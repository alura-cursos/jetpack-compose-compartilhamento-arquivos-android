package com.alura.concord.media

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okio.use
import java.io.File
import java.io.InputStream

fun Long.formatReadableFileSize(): String {
    val size = this
    val kilobyte = 1024
    val megaByte = kilobyte * 1024
    val gigaByte = megaByte * 1024

    return when {
        size < kilobyte -> "$size B"
        size < megaByte -> "${size / kilobyte} KB"
        size < gigaByte -> "${size / megaByte} MB"
        else -> "${size / gigaByte} GB"
    }
}


suspend fun Context.saveOnInternalStorage(
    inputStream: InputStream,
    fileName: String,
    onSuccess: (String) -> Unit
) {
    val path = getExternalFilesDir("temp")
    val newFile = File(path, fileName)

    withContext(IO) {
        newFile.outputStream().use { file ->
            inputStream.copyTo(file)
        }

        if (newFile.exists()) {
            onSuccess(newFile.path)
        }
    }
}


fun Context.openWith(mediaLink: String){
    val shareIntent = Intent().apply {
        action = Intent.ACTION_VIEW
        putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaLink))
        type = "image/*"
    }
    startActivity(Intent.createChooser(shareIntent,"Abrir com"))

}