package com.example.photosearchproject.data.model

import com.google.gson.annotations.SerializedName

data class PhotosSearchResponseModel(
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("photos")
    val photos: List<PhotoResponseModel>,
    @SerializedName("next_page")
    val nextPage: String?
)

data class PhotoResponseModel(
    @SerializedName("id")
    val id: Long,
    @SerializedName("url")
    val url: String,
    @SerializedName("photographer")
    val photographer: String,
    @SerializedName("src")
    val src: PhotoSourceResponseModel
)

data class PhotoSourceResponseModel(
    @SerializedName("original")
    val original: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("landscape")
    val landscape: String,
)