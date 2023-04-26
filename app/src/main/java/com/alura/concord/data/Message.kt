package com.alura.concord.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0L,
    val chatId: Long = 0L,
    val content: String = "",
    val author: Author = Author.OTHER,
    val date: String = "",
    val mediaLink: String = ""
)


enum class Author {
    USER, OTHER
}
