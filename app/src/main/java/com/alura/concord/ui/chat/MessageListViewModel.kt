package com.alura.concord.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.concord.data.DownloadStatus
import com.alura.concord.data.FileInDownload
import com.alura.concord.data.MessageWithFile
import com.alura.concord.data.toMessageEntity
import com.alura.concord.database.ChatDao
import com.alura.concord.database.DownloadableFileDao
import com.alura.concord.database.MessageDao
import com.alura.concord.database.entities.Author
import com.alura.concord.database.entities.MessageEntity
import com.alura.concord.database.entities.toDownloadableFile
import com.alura.concord.database.entities.toMessageFile
import com.alura.concord.navigation.messageChatIdArgument
import com.alura.concord.util.getFormattedCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val downloadableFileDao: DownloadableFileDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(MessageListUiState())
    val uiState: StateFlow<MessageListUiState>
        get() = _uiState.asStateFlow()

    private var chatId: Long =
        requireNotNull(savedStateHandle.get<String>(messageChatIdArgument)?.toLong())

    init {
        loadDatas()

        _uiState.update { state ->
            state.copy(
                onMessageValueChange = {
                    _uiState.value = _uiState.value.copy(
                        messageValue = it
                    )

                    _uiState.value = _uiState.value.copy(
                        hasContentToSend = (it.isNotEmpty() || _uiState.value.mediaInSelection.isNotEmpty())
                    )
                },

                onMediaInSelectionChange = {
                    _uiState.value = _uiState.value.copy(
                        mediaInSelection = it
                    )
                    _uiState.value = _uiState.value.copy(
                        hasContentToSend = (it.isNotEmpty() || _uiState.value.messageValue.isNotEmpty())
                    )
                }
            )
        }
    }

    private fun loadDatas() {
        loadChatsInfos()
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            messageDao.getByChatId(chatId).collect { messages ->
                val mapMensagens = messages.map { searchedMessage ->
                    if (searchedMessage.author == Author.OTHER) {
                        loadMessageWithDownloadableFile(
                            searchedMessage.toMessageFile()
                        )
                    } else {
                        searchedMessage.toMessageFile()
                    }
                }

                _uiState.value = _uiState.value.copy(
                    messages = mapMensagens.filterNotNull()
                )
            }
        }
    }

    private suspend fun loadMessageWithDownloadableFile(
        searchedMessage: MessageWithFile
    ): MessageWithFile? {
        return searchedMessage.idDownloadableFile?.let { contentId ->
            val downloadableFileEntity = downloadableFileDao.getById(contentId).first()
            searchedMessage.copy(
                downloadableFile = downloadableFileEntity?.toDownloadableFile()
            )
        }
    }

    private fun loadChatsInfos() {
        viewModelScope.launch {
            val chat = chatDao.getById(chatId).first()
            chat?.let {
                _uiState.value = _uiState.value.copy(
                    ownerName = chat.owner,
                    profilePicOwner = chat.profilePicOwner
                )
            }
        }
    }

    private fun saveMessage(
        userMessage: MessageEntity
    ) {
        viewModelScope.launch {
            userMessage.let { messageDao.insert(it) }
        }
    }

    private fun cleanFields() {
        _uiState.value = _uiState.value.copy(
            messageValue = "",
            mediaInSelection = "",
            hasContentToSend = false
        )
    }

    fun sendMessage() {
        with(_uiState) {
            if (!value.hasContentToSend) {
                return
            }

            val userMessageEntity = MessageEntity(
                content = value.messageValue,
                author = Author.USER,
                chatId = chatId,
                mediaLink = value.mediaInSelection,
                date = getFormattedCurrentDate(),
            )
            saveMessage(userMessageEntity)
            cleanFields()
        }
    }

    fun loadMediaInScreen(
        path: String
    ) {
        _uiState.value.onMediaInSelectionChange(path)
    }

    fun deselectMedia() {
        _uiState.value = _uiState.value.copy(
            mediaInSelection = "",
            hasContentToSend = false
        )
    }


    fun setShowBottomSheetSticker(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            showBottomSheetSticker = value,
        )
    }

    fun setShowBottomSheetFile(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            showBottomSheetFile = value,
        )
    }


    fun setShowBottomShareSheet(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            showBottomShareSheet = value,
        )
    }


    fun setShowFileOptions(messageId: Long? = null, show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showBottomShareSheet = show,
            selectedMessage = _uiState.value.messages.firstOrNull { it.id == messageId }
                ?: MessageWithFile()
        )
    }

    fun startDownload(messageWithDownload: MessageWithFile) {

        val updatedMessages = _uiState.value.messages.map { message ->
            if (message.id == messageWithDownload.id) {
                message.copy(
                    downloadableFile = message.downloadableFile?.copy(
                        status = DownloadStatus.DOWNLOADING
                    )
                )
            } else {
                message
            }
        }

        val fileInDownload = messageWithDownload.downloadableFile?.let {
            FileInDownload(
                messageId = messageWithDownload.id,
                url = it.url,
                name = it.name,
                inputStream = null
            )
        }

        fileInDownload?.let {
            _uiState.value = _uiState.value.copy(
                messages = updatedMessages,
                fileInDownload = fileInDownload
            )

        }
    }


    fun finishDownload(messageId: Long, contentPath: String) {
        var messageWithoutContentDownload: MessageWithFile

        val menssagens = _uiState.value.messages.map { message ->
            if (message.id == messageId) {
                messageWithoutContentDownload = message.copy(
                    idDownloadableFile = 0,
                    downloadableFile = null,
                    mediaLink = contentPath,
                )
                updateSingleMessage(messageWithoutContentDownload.toMessageEntity())
                messageWithoutContentDownload
            } else {
                message
            }
        }

        _uiState.value = _uiState.value.copy(
            fileInDownload = null,
            messages = menssagens
        )
    }

    fun failureDownload(messageId: Long) {
        val updatedMessages = _uiState.value.messages.map { message ->
            if (message.id == messageId) {
                message.copy(
                    downloadableFile = message.downloadableFile?.copy(
                        status = DownloadStatus.ERROR
                    )
                )
            } else {
                message
            }
        }

        _uiState.value = _uiState.value.copy(
            messages = updatedMessages,
            fileInDownload = null
        )
    }

    private fun updateSingleMessage(messageEntity: MessageEntity) {
        viewModelScope.launch {
            messageDao.insert(messageEntity)
        }
    }


    fun downloadInProgress(): Boolean {
        return _uiState.value.fileInDownload == null
    }
}