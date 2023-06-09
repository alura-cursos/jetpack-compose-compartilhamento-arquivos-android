package com.alura.concord.data

import java.io.InputStream

data class FileInDownload(
    var name: String = "",
    var url: String = "",
    var inputStream: InputStream? = null,
    var messageId: Long = 0L,
)