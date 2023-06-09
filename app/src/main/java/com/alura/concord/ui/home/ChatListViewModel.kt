package com.alura.concord.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.concord.data.Chat
import com.alura.concord.database.ChatDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatDao: ChatDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState>
        get() = _uiState.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            delay(Random.nextLong(100, 800))
            chatDao.getAllWithLastMessage().collect { chatListWithLastMessage ->
                val chatList = chatListWithLastMessage.map { chatWithLastMessage ->
                    with(chatWithLastMessage) {
                        Chat(
                            id = chat.id,
                            owner = chat.owner,
                            profilePicOwner = chat.profilePicOwner,
                            lastMessage = lastMessage ?: "",
                            dateLastMessage = dateLastMessage ?: ""
                        )
                    }
                }

                _uiState.value = _uiState.value.copy(
                    chats = chatList,
                    isLoading = false
                )
            }
        }
    }
}