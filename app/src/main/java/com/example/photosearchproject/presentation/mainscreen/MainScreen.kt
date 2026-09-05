package com.example.photosearchproject.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.photosearchproject.R
import com.example.photosearchproject.domain.model.Photo
import com.example.photosearchproject.presentation.components.CustomToolbar
import com.example.photosearchproject.presentation.components.ToolbarType
import com.example.photosearchproject.presentation.mainscreen.Dimens.HORIZONTAL_PADDING
import com.example.photosearchproject.presentation.mainscreen.Dimens.ITEM_AUTHOR_TEXT_PADDING
import com.example.photosearchproject.presentation.mainscreen.Dimens.ITEM_ELEVATION
import com.example.photosearchproject.presentation.mainscreen.Dimens.ITEM_HEIGHT
import com.example.photosearchproject.presentation.mainscreen.Dimens.ITEM_IMAGE_PART_HEIGHT
import com.example.photosearchproject.presentation.mainscreen.Dimens.ITEM_TOP_PADDING
import com.example.photosearchproject.presentation.mainscreen.Dimens.LOADING_INDICATOR_PADDING
import com.example.photosearchproject.presentation.mainscreen.Dimens.LOADING_INDICATOR_SIZE

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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(MAIN_SCREEN_LOADING_INDICATOR_TAG),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MainScreenState.Success -> {
                PhotoList(
                    photos = screenState.photos,
                    hasReachedLastPage = screenState.hasReachedLastPage,
                    onPhotoClick = onPhotoClick,
                    onFetchNext = onFetchNext
                )
            }

            is MainScreenState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(MAIN_SCREEN_ERROR_TAG),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.error_message, screenState.message),
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
    hasReachedLastPage: Boolean,
    onPhotoClick: (String) -> Unit,
    onFetchNext: () -> Unit
) {
    val state = rememberLazyListState()
    val loadMore by remember {
        derivedStateOf {
            val size = state.layoutInfo.totalItemsCount
            val lastVisible = state.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            !hasReachedLastPage && size > 0 && lastVisible >= size - THRESHOLD
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
            .padding(horizontal = HORIZONTAL_PADDING)
            .testTag(MAIN_SCREEN_PHOTO_LIST_TAG),
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(photos, key = { _, item -> item.id }) { _, item ->
            PhotoItem(item, onPhotoClick)
        }

        if (!hasReachedLastPage) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(LOADING_INDICATOR_PADDING),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(LOADING_INDICATOR_SIZE))
                }
            }
        }
    }
}

@Composable
fun PhotoItem(photo: Photo, onClick: (String) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = ITEM_TOP_PADDING)
            .fillMaxWidth()
            .height(ITEM_HEIGHT)
            .testTag(MAIN_SCREEN_ITEM_TAG),
        elevation = CardDefaults.cardElevation(defaultElevation = ITEM_ELEVATION)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ITEM_IMAGE_PART_HEIGHT)
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
                    .padding(ITEM_AUTHOR_TEXT_PADDING),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                text = photo.photographerName
            )
        }
    }
}

const val MAIN_SCREEN_LOADING_INDICATOR_TAG = "loading_indicator"
const val MAIN_SCREEN_PHOTO_LIST_TAG = "photo_indicator"
const val MAIN_SCREEN_ERROR_TAG = "error_indicator"
const val MAIN_SCREEN_ITEM_TAG = "item_indicator"