package com.alura.concord.media

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