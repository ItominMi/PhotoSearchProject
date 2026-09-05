package com.example.photosearchproject.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.photosearchproject.domain.model.Photo
import com.example.photosearchproject.presentation.components.CustomToolbar
import com.example.photosearchproject.presentation.components.ToolbarType

private const val THRESHOLD = 5

@Composable
fun MainScreen(
    onNavigateToPhotoScreen: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MainScreenContent(
        screenState = state,
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CustomToolbar(ToolbarType.Search(onSearchChanged))

        when (screenState) {
            is MainScreenState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is MainScreenState.Success -> {
                PhotoList(
                    photos = screenState.photos,
                    onPhotoClick = onPhotoClick,
                    onFetchNext = onFetchNext
                )
            }

            is MainScreenState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${screenState.message}",
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoList(
    photos: List<Photo>,
    onPhotoClick: (String) -> Unit,
    onFetchNext: () -> Unit
) {
    val state = rememberLazyListState()
    val loadMore by remember {
        derivedStateOf {
            val size = state.layoutInfo.totalItemsCount
            val lastVisible = state.layoutInfo.visibleItemsInfo.lastIndex
            size > 0 && lastVisible >= size - THRESHOLD
        }
    }

    LaunchedEffect(loadMore) {
        if (loadMore) {
            onFetchNext()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(photos, key = { _, item -> item.id }) { _, item ->
            PhotoItem(item, onPhotoClick)
        }
    }
}

@Composable
fun PhotoItem(photo: Photo, onClick: (String) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .height(230.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        onClick(photo.originalUrl)
                    },
                contentScale = ContentScale.FillWidth,
                model = photo.mediumUrl,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(4.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                text = photo.photographerName
            )
        }
    }
}