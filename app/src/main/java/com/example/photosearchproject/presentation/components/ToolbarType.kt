package com.example.photosearchproject.presentation.components

sealed interface ToolbarType {
    data class Search(val onSearchChanged: (String) -> Unit) : ToolbarType
    data class Back(val onBack: () -> Unit) : ToolbarType
}