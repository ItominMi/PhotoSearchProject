package com.example.photosearchproject.data.network

import com.example.photosearchproject.data.model.PhotosSearchResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApiService {

    @GET("v1/search")
    suspend fun getPhotos(
        @Query("query") query: String,
        @Query("per_page") pageLimit: Int,
        @Query("page") page: Int
    ): Response<PhotosSearchResponseModel>

}