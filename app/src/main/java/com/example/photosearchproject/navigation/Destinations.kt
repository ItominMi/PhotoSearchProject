package com.example.photosearchproject.navigation

import kotlinx.serialization.Serializable

@Serializable
object MainScreen

@Serializable
data class PhotoDetailScreen(val photoUrl: String)