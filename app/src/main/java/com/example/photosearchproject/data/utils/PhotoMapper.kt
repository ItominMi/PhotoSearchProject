package com.example.photosearchproject.data.utils

import com.example.photosearchproject.data.model.PhotoResponseModel
import com.example.photosearchproject.domain.model.Photo

fun PhotoResponseModel.toPhoto() = Photo(
    id = id,
    photographerName = photographer,
    mediumUrl = this.src.medium,
    originalUrl = this.src.original
)