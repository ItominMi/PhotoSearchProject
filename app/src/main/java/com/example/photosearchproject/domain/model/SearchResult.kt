package com.example.photosearchproject.domain.model

data class SearchResult(
    val photos: List<Photo>,
    val hasNextPage: Boolean
)
