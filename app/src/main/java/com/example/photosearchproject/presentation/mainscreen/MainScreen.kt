package com.example.photosearchproject.presentation.mainscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainScreen(
    onNavigateToPhotoScreen: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MainScreenContent(
        state,
        onPhotoClick = onNavigateToPhotoScreen,
        onSearchChanged = { query -> viewModel.handleIntent(MainScreenIntent.Search(query)) },
        onFetchNext = { viewModel.handleIntent(MainScreenIntent.FetchNext) })
}

@Composable
fun MainScreenContent(
    screenState: MainScreenState,
    onPhotoClick: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    onFetchNext: () -> Unit
) {

}