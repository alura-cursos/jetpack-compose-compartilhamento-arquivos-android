package com.alura.concord.navigation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alura.concord.extensions.showMessage
import com.alura.concord.media.getAllImages
import com.alura.concord.media.getNameByUri
import com.alura.concord.media.imagePermission
import com.alura.concord.media.openWith
import com.alura.concord.media.persistUriPermission
import com.alura.concord.media.saveOnExternalStorage
import com.alura.concord.media.saveOnInternalStorage
import com.alura.concord.media.shareFile
import com.alura.concord.media.verifyPermission
import com.alura.concord.ui.chat.MessageListViewModel
import com.alura.concord.ui.chat.MessageScreen
import com.alura.concord.ui.components.ModalBottomSheetFile
import com.alura.concord.ui.components.ModalBottomShareSheet
import com.alura.concord.ui.components.ModalBottomSheetSticker
import java.io.File

internal const val messageChatRoute = "messages"
internal const val messageChatIdArgument = "chatId"
internal const val messageChatFullPath = "$messageChatRoute/{$messageChatIdArgument}"


fun NavGraphBuilder.messageListScreen(
    onBack: () -> Unit = {},
) {
    composable(messageChatFullPath) { backStackEntry ->
        backStackEntry.arguments?.getString(messageChatIdArgument)?.let { chatId ->
            val viewModelMessage = hiltViewModel<MessageListViewModel>()
            val uiState by viewModelMessage.uiState.collectAsState()
            val context = LocalContext.current

            val requestPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModelMessage.setShowBottomSheetSticker(true)
                } else {
                    context.showMessage(
                        "Permissão não concedida, não será possivel acessar os stickers sem ela",
                        true
                    )
                }
            }

            LaunchedEffect(uiState.fileInDownload) {
                uiState.fileInDownload?.let { fileInDownload ->
                    fileInDownload.inputStream?.let { inputStream ->
                        context.saveOnInternalStorage(
                            inputStream = inputStream,
                            fileName = fileInDownload.name,
                            onSuccess = { filePath ->
                                viewModelMessage.finishDownload(
                                    messageId = fileInDownload.messageId,
                                    contentPath = filePath
                                )
                            },
                            onFailure = {
                                viewModelMessage.failureDownload(
                                    messageId = fileInDownload.messageId
                                )
                            }
                        )
                    }
                }
            }

            MessageScreen(
                state = uiState,
                onSendMessage = {
                    viewModelMessage.sendMessage()
                },
                onShowSelectorFile = {
                    viewModelMessage.setShowBottomSheetFile(true)
                },
                onShowSelectorStickers = {
                    if (context.verifyPermission(imagePermission())) {
                        requestPermissionLauncher.launch(imagePermission())
                    } else {
                        viewModelMessage.setShowBottomSheetSticker(true)
                    }
                },
                onDeselectMedia = {
                    viewModelMessage.deselectMedia()
                },
                onBack = {
                    onBack()
                },
                onContentDownload = { message ->
                    if (viewModelMessage.downloadInProgress()) {
                        viewModelMessage.startDownload(message)
                    } else {
                        context.showMessage(
                            "Aguarde o download terminar para baixar outro arquivo", true
                        )
                    }
                },
                onShowFileOptions = { selectedMessage ->
                    viewModelMessage.setShowFileOptions(selectedMessage.id, true)
                }
            )

            if (uiState.showBottomSheetSticker) {

                val stickerList = mutableStateListOf<String>()

                context.getAllImages(onLoadImages = { images ->
                    stickerList.addAll(images)
                })

                ModalBottomSheetSticker(
                    stickerList = stickerList,
                    onSelectedSticker = {
                        viewModelMessage.setShowBottomSheetSticker(false)
                        viewModelMessage.loadMediaInScreen(path = it.toString())
                        viewModelMessage.sendMessage()
                    },
                    onBack = {
                        viewModelMessage.setShowBottomSheetSticker(false)
                    }
                )
            }

            val pickMedia = rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    context.persistUriPermission(uri)
                    viewModelMessage.loadMediaInScreen(uri.toString())
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

            val pickFile = rememberLauncherForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri != null) {
                    context.persistUriPermission(uri)

                    val name = context.getNameByUri(uri)

                    uiState.onMessageValueChange(name.toString())
                    viewModelMessage.loadMediaInScreen(uri.toString())
                    viewModelMessage.sendMessage()
                } else {
                    Log.d("FilePicker", "No media selected")
                }
            }

            if (uiState.showBottomSheetFile) {
                ModalBottomSheetFile(
                    onSelectPhoto = {
                        pickMedia.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageAndVideo
                            )
                        )
                        viewModelMessage.setShowBottomSheetFile(false)
                    },
                    onSelectFile = {
                        pickFile.launch(arrayOf("*/*"))
                        viewModelMessage.setShowBottomSheetFile(false)
                    },
                    onBack = {
                        viewModelMessage.setShowBottomSheetFile(false)
                    }
                )
            }


            val createFile = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument("*/*"),
                onResult = {
                    it?.let { uri: Uri ->
                        val mediaToOpen = uiState.selectedMessage.mediaLink
                        context.saveOnExternalStorage(mediaToOpen, uri)
                    }
                }
            )

            if (uiState.showBottomShareSheet) {
                val mediaToOpen = uiState.selectedMessage.mediaLink

                ModalBottomShareSheet(
                    onOpenWith = {
                        context.openWith(mediaToOpen)
                    },
                    onShare = {
                        context.shareFile(mediaToOpen)
                    },
                    onSave = {
                        val fileName = File(mediaToOpen).name
                        createFile.launch(fileName)
                    },
                    onBack = {
                        viewModelMessage.setShowBottomShareSheet(false)
                    }
                )
            }
        }
    }
}

internal fun NavHostController.navigateToMessageScreen(
    chatId: Long, navOptions: NavOptions? = null
) {
    navigate("$messageChatRoute/$chatId", navOptions)
}

