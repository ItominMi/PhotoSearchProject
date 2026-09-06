package com.example.photosearchproject.domain.model

data class Photo(
    val id: Long,
    val photographerName: String,
    val mediumUrl: String,
    val originalUrl: String
)