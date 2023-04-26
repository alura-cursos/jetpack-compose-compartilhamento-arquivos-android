package com.alura.concord.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val owner: String = "",
    val profilePicOwner: String = "",
    @Ignore
    val lastMessage: String = "",
    @Ignore
    var dateLastMessage: String = "",
) {
    constructor(
        id: Long = 0L,
        owner: String = "",
        profilePicOwner: String = ""
    ) : this(
        id = id,
        owner = owner,
        profilePicOwner = profilePicOwner,
        lastMessage = "",
        dateLastMessage = ""
    )
}


data class ChatWithLastMessage(
    @Embedded val chat: Chat,
    val lastMessage: String?,
    val dateLastMessage: String?
)