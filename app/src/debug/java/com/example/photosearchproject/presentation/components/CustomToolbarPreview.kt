package com.example.photosearchproject.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CustomToolbarSearchPreview() {
    CustomToolbar(type = ToolbarType.Search(onSearchChanged = {}))
}

@Preview
@Composable
fun CustomToolbarBackPreview() {
    CustomToolbar(type = ToolbarType.Back(onBack = {}))
}