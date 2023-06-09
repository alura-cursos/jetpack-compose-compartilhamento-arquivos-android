package com.alura.concord.data


data class DownloadableFile(
    var id: Long = 0L,
    var name: String = "",
    var url: String = "",
    var size: Long = 0L,
    var status: DownloadStatus = DownloadStatus.PENDING,
)


enum class DownloadStatus {
    PENDING, DOWNLOADING, ERROR
}
