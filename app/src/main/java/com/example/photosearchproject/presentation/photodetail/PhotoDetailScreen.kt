package com.example.photosearchproject.presentation.photodetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.photosearchproject.presentation.components.CustomToolbar
import com.example.photosearchproject.presentation.components.ToolbarType

@Composable
fun PhotoDetailScreen(
    url: String,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CustomToolbar(ToolbarType.Back(onBack))
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = url,
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
    }
}