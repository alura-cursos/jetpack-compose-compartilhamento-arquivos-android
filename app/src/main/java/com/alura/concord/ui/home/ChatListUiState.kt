package com.alura.concord.ui.home

import com.alura.concord.data.Chat

data class ChatListUiState(
    val selectedId: Long? = null,
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = true
)