package com.alura.concord.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.concord.data.Author
import com.alura.concord.data.DownloadStatus
import com.alura.concord.data.DownloadableContent
import com.alura.concord.data.Message
import com.alura.concord.data.messageListSample
import com.alura.concord.database.ChatDao
import com.alura.concord.database.DownloadableContentDao
import com.alura.concord.database.MessageDao
import com.alura.concord.navigation.messageChatIdArgument
import com.alura.concord.util.getFormattedCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val downloadableContentDao: DownloadableContentDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(MessageListUiState())
    val uiState: StateFlow<MessageListUiState>
        get() = _uiState.asStateFlow()

    private var chatId: Long =
        requireNotNull(savedStateHandle.get<String>(messageChatIdArgument)?.toLong())

    init {
//        initWithSamples()
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

    private fun initWithSamples() {
        _uiState.value = _uiState.value.copy(
            messages = messageListSample,
        )
        loadChatsInfos()
    }

    private fun loadDatas() {
        loadChatsInfos()
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            messageDao.getByChatId(chatId).collect { messages ->
                messages.forEach { searchedMessage ->
                    if (searchedMessage.author == Author.OTHER) {
                        loadMessageWithDownloadableContent(searchedMessage)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            messages = _uiState.value.messages + searchedMessage
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadMessageWithDownloadableContent(searchedMessage: Message) {
        searchedMessage.idDownloadableContent?.let { contentId ->
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages + searchedMessage.copy(
                    downloadableContent = downloadableContentDao.getById(contentId)
                        .first()
                )
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
        userMessage: Message
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

            val userMessage = Message(
                content = value.messageValue,
                author = Author.USER,
                chatId = chatId,
                mediaLink = value.mediaInSelection,
                date = getFormattedCurrentDate(),
            )
            saveMessage(userMessage)
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

    fun setImagePermission(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            hasImagePermission = value,
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

    fun simulatedDownload(currentMessage: Message) {
        viewModelScope.launch {
            initDownload(currentMessage.id)

            delay(2000)
            val messageWithDownload = currentMessage.copy(
                idDownloadableContent = 0,
                mediaLink = currentMessage.downloadableContent?.url ?: "",
                downloadableContent = null
            )

            // atualizar lista de messagens geral sendo que a mensagem atualizada é a que tem o id igual ao da mensagem com download
            _uiState.value = _uiState.value.copy(
                messages = _uiState.value.messages.map { message ->
                    if (message.id == messageWithDownload.id) {
                        messageWithDownload
                    } else {
                        message
                    }
                }
            )
        }
    }

    private fun initDownload(messageId: Long) {

        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages.map { message ->
                if (message.id == messageId) {
                    message.copy(
                        downloadableContent = DownloadableContent(
                            status = DownloadStatus.DOWNLOADING
                        )
                    )
                } else {
                    message
                }
            }
        )
    }
}