package com.example.photosearchproject.presentation.mainscreen

import com.example.photosearchproject.domain.model.Photo

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data class Error(val message: String) : MainScreenState()
    data class Success(
        val photos: List<Photo>,
        val hasReachedLastPage: Boolean = false
    ) : MainScreenState()
}

sealed class MainScreenIntent {
    data class Search(val query: String) : MainScreenIntent()
    data object FetchNext : MainScreenIntent()
}