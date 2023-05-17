package com.alura.concord.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url


fun gitHubgetFileSizeInKB(url: String, callback: (Long?) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://github.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val fileApi = retrofit.create(FileApi::class.java)

    fileApi.getFileSize(url, "bytes=0-0").enqueue(object : retrofit2.Callback<String> {
        override fun onResponse(
            call: retrofit2.Call<String>,
            response: retrofit2.Response<String>
        ) {
            val fileSize =
                response.headers()["Content-Range"]?.substringAfterLast('/')?.toLongOrNull()
                    ?: -1L
            callback(fileSize / 1024)
        }

        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
            callback(null)
        }
    })
}


private interface FileApi {
    @GET
    fun getFileSize(@Url url: String, @Header("Range") range: String): retrofit2.Call<String>
}


const val file7KB =
    "https://github.com/git-jr/sample-files/raw/main/stickers/Emoji%207%20Android%20Ice%20Cream.png"
const val file92KB =
    "https://github.com/git-jr/sample-files/raw/main/audios/%C3%81udio%20teste%201.mp3"
const val fileMore1MB = "https://github.com/git-jr/sample-files/raw/main/documents/Realatorio.pdf"
const val fileMore700MB =
    "https://download1654.mediafire.com/hfq3t5bcncogN7WJLOZ05HidQUJ_qcRxq0fFbdM5qYF2DJAGunbXJ-8HNUawhVl9UhIfAXSoOFhOXYScGgL7Qv00AAN7lr9KM95V0kkDkhbhfB_CNfmLQOu-qJPsbOk3PoSUoT24OXUxHFuYVhVlOez6ZadaZGQ9wMc7qouknDg8HA/x92f6bwfas1n703/Projetos+e+M%C3%ADdias+layout+Paradoxo+2021.zip"
const val fileInGB =
    "https://releases.ubuntu.com/22.04.2/ubuntu-22.04.2-desktop-amd64.iso?_ga=2.6087732.66032191.1684249590-1145850122.1684249590"


fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> {
            "$size KB"
        }

        size < 1024 * 1024 -> {
            "${size / 1024} MB"
        }

        else -> {
            "${size / (1024 * 1024)} GB"
        }
    }
}