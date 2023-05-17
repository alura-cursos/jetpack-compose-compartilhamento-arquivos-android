package com.alura.concord.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var chatId: Long = 0L,
    var content: String = "",
    var author: Author = Author.OTHER,
    var date: String = "",
    var mediaLink: String = "",
    @ColumnInfo(defaultValue = "0")
    var idDownloadableContent: Long? = null,
    @Ignore
    var downloadableContent: DownloadableContent? = null
) {
    constructor() : this(0L, 0L, "", Author.OTHER, "", "", null)
}

enum class Author {
    USER, OTHER
}

