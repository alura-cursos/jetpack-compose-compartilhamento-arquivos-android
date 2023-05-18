package com.alura.concord.media

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns

fun Context.getAllImages(onLoadImages: (List<String>) -> Unit) {
    val images = mutableListOf<String>()

    val projection = arrayOf(
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
    )
    val selection = "${MediaStore.Images.Media.DATA} LIKE '%/Download/stickers/%' " +
            "AND ${MediaStore.Images.Media.SIZE} > ?"
    val selectionArgs = arrayOf("70000")
    val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} DESC"

    contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use { cursor ->

        while (cursor.moveToNext()) {
            val pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val path = cursor.getString(pathIndex)

            images.add(path)
        }
        onLoadImages(images)
    }
}


fun Context.getNameByUri(uri: Uri): String? {
    return contentResolver.query(uri, null, null, null, null)
        .use { cursor ->
            val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor?.moveToFirst()
            nameIndex?.let { cursor.getString(it) }
        }
}
