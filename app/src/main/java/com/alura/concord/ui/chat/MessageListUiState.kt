package com.alura.concord.ui.chat

import com.alura.concord.data.FileInDownload
import com.alura.concord.data.MessageWithFile

data class MessageListUiState(
    val messages: List<MessageWithFile> = emptyList(),
    val messageValue: String = "",
    val onMessageValueChange: (String) -> Unit = {},
    val onMediaInSelectionChange: (String) -> Unit = {},
    val hasContentToSend: Boolean = false,
    val onHasContentToSend: (Boolean) -> Unit = {},
    val error: String = "",
    val mediaInSelection: String = "",
    val profilePicOwner: String = "",
    val ownerName: String = "",
    val hasImagePermission: Boolean = false,
    val showBottomSheetSticker: Boolean = false,
    val showBottomSheetFile: Boolean = false,
    val showBottomShareSheet: Boolean = false,
    val onMakeContentDownload: (Boolean) -> Unit = {},
    val selectedMessage: MessageWithFile = MessageWithFile(),
    val fileInDownload: FileInDownload? = null,
)