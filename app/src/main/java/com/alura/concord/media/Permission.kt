package com.alura.concord.media

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat

fun imagePermission() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_IMAGES
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}

fun Context.persistUriPermission(uri: Uri) {
    val contentResolver = contentResolver
    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
    contentResolver.takePersistableUriPermission(uri, takeFlags)
}

fun Context.verifyPermission(permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED)
}
