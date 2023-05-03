package com.alura.concord.navigation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alura.concord.extensions.showLog
import com.alura.concord.extensions.showMessage
import com.alura.concord.ui.chat.MessageListViewModel
import com.alura.concord.ui.chat.MessageScreen
import com.alura.concord.ui.components.ModalBottomSheetFile
import com.alura.concord.ui.components.ModalBottomSheetSticker

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

            MessageScreen(
                state = uiState,
                onSendMessage = {
                    viewModelMessage.sendMessage()
                },
                onShowSelectorFile = {
                    viewModelMessage.setShowBottomSheetFile(true)
                },
                onShowSelectorStickers = {
                    viewModelMessage.setShowBottomSheetSticker(true)
                },
                onDeselectMedia = {
                    viewModelMessage.deselectMedia()
                },
                onBack = {
                    onBack()
                }
            )

            val requestPermissionLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        context.showMessage("Permissão concedida")
                    } else {
                        context.showMessage("Permissão NÃO concedida")
                    }
                }


            if (uiState.showBottomSheetSticker) {

                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

                requestPermissionLauncher.launch(permission)

                val stickerList = mutableStateListOf<String>()

                getAllImages(context, onLoadImages = { images ->
                    stickerList.addAll(images)
                })

//                context.getExternalFilesDir("stickers")?.listFiles()?.forEach { file ->
//                    stickerList.add(file.path)
//                }

                ModalBottomSheetSticker(
                    stickerList = stickerList,
                    onSelectedSticker = {
                        viewModelMessage.setShowBottomSheetSticker(false)
                        viewModelMessage.loadMediaInScreen(path = it.toString())
                        viewModelMessage.sendMessage()
                    }, onBack = {
                        viewModelMessage.setShowBottomSheetSticker(false)
                    })
            }

            val pickMedia = rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    val contentResolver = context.contentResolver
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(uri, takeFlags)

                    viewModelMessage.loadMediaInScreen(uri.toString())
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

            val pickFile = rememberLauncherForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri != null) {

                    val contentResolver = context.contentResolver
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(uri, takeFlags)

                    val name = context.contentResolver.query(uri, null, null, null, null)
                        .use { cursor ->
                            val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            cursor?.moveToFirst()
                            nameIndex?.let { cursor?.getString(it) }
                        }


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
                    }, onBack = {
                        viewModelMessage.setShowBottomSheetFile(false)
                    })
            }
        }
    }
}

private fun getAllImages(context: Context, onLoadImages: (List<String>) -> Unit) {
    val images = mutableListOf<String>()

    val projection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
    )
    val selection = "${MediaStore.Images.Media.DATA} LIKE '%/Download/stickers/%' " +
            "AND ${MediaStore.Images.Media.SIZE} < ?"
    val selectionArgs = arrayOf("70000")
    val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use { cursor ->

        while (cursor.moveToNext()) {
            val nameIndex: Int = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            val name: String = cursor.getString(nameIndex)

            val pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val path = cursor.getString(pathIndex)

            context.showLog("Nome: $name e caminho: $path")
            images.add(path)
        }
        onLoadImages(images)
    }
}


internal fun NavHostController.navigateToMessageScreen(
    chatId: Long,
    navOptions: NavOptions? = null
) {
    navigate("$messageChatRoute/$chatId", navOptions)
}

