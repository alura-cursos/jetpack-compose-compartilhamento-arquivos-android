package com.alura.concord.network

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.use
import java.io.InputStream

object DownloadService {

    suspend fun makeDownloadByURL(
        url: String,
        onFinishedDownload: (InputStream) -> Unit
    ) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        withContext(IO) {
            client.newCall(request).execute().use { response ->
                response.body?.byteStream()?.use { fileData: InputStream ->
                    onFinishedDownload(fileData)
                }
            }
        }
    }
}