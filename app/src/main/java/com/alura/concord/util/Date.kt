package com.alura.concord.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random

fun getFormattedCurrentDate(): String {
    val currentTime = LocalDateTime.now()
    val dateFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    return dateFormat.format(currentTime)
}

fun getRandomDate(): String {
    val hour = Random.nextInt(0, 24)
    val minute = Random.nextInt(0, 60)
    return String.format("%02d:%02d", hour, minute)
}