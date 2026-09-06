package com.example.photosearchproject.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.photosearchproject.R
import com.example.photosearchproject.presentation.components.Dimens.CustomToolbar.ICON_START_PADDING
import com.example.photosearchproject.presentation.components.Dimens.CustomToolbar.TOOLBAR_HEIGHT
import com.example.photosearchproject.presentation.theme.ToolbarColor

@Composable
fun CustomToolbar(type: ToolbarType) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOOLBAR_HEIGHT)
            .background(ToolbarColor)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (type) {
            is ToolbarType.Search -> {
                var text by remember { mutableStateOf("") }
                Icon(
                    modifier = Modifier
                        .padding(start = ICON_START_PADDING),
                    imageVector = Icons.Default.Search,
                    tint = Color.White,
                    contentDescription = null
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    value = text,
                    onValueChange = {
                        text = it
                        type.onSearchChanged(it)
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.toolbar_search_hint),
                            color = Color.White
                        )
                    })
            }

            is ToolbarType.Back -> {
                Icon(
                    modifier = Modifier
                        .padding(start = ICON_START_PADDING)
                        .clickable { type.onBack() },
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    }
}