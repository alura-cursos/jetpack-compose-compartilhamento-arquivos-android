package com.alura.concord.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class DownloadableContent(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var url: String = "",
    var size: Long = 0L,
    @Ignore
    var status: DownloadStatus = DownloadStatus.PENDING,
) {
    constructor() : this(0L, "", 0L)
}


enum class DownloadStatus {
    PENDING, DOWNLOADING, DOWNLOADED, ERROR
}
