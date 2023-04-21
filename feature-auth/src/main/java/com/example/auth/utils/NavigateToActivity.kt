package com.example.auth.utils

import android.content.Context
import android.content.Intent


fun <T> Context.navigateToActivity(activity: Class<T>) {
    val intent = Intent(this, activity ).apply {
        flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    startActivity(intent)
}


