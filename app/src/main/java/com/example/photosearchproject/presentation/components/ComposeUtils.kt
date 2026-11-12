package com.example.photosearchproject.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.debounceClickable(
    debounceTime: Long = 600,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTimeStamp by remember { mutableLongStateOf(0L) }

    clickable {
        val currentTimeStamp = System.currentTimeMillis()
        if(currentTimeStamp - lastClickTimeStamp >= debounceTime) {
            lastClickTimeStamp = currentTimeStamp
            onClick()
        }
    }
}