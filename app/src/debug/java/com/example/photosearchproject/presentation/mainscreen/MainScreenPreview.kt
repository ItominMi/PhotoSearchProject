package com.example.photosearchproject.presentation.mainscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MainScreenLoadingPreview() {
    MainScreenContent(
        screenState = MainScreenState.Loading,
        onPhotoClick = {},
        onSearchChanged = {},
        onFetchNext = {})
}

@Preview
@Composable
fun MainScreenErrorPreview() {
    MainScreenContent(
        screenState = MainScreenState.Error("Mock Error"),
        onPhotoClick = {},
        onSearchChanged = {},
        onFetchNext = {})
}

@Preview
@Composable
fun MainScreenSuccessPreview() {
    MainScreenContent(
        screenState = MainScreenState.Success(
            photos = emptyList(),
        ),
        onPhotoClick = {},
        onSearchChanged = {},
        onFetchNext = {})
}