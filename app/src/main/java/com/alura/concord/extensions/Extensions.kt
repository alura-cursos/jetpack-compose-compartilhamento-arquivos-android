package com.alura.concord.extensions

import android.content.Context
import android.widget.Toast

fun Context.showMessage(message: String, longTime: Boolean = false) {
    Toast.makeText(
        this,
        message,
        if (longTime) {
            Toast.LENGTH_LONG
        } else {
            Toast.LENGTH_SHORT
        }
    ).show()
}