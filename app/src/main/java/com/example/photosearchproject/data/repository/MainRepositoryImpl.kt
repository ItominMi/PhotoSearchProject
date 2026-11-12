package com.example.photosearchproject.data.repository

import com.example.photosearchproject.data.network.PhotosApiService
import com.example.photosearchproject.data.utils.decode
import com.example.photosearchproject.data.utils.toPhoto
import com.example.photosearchproject.domain.model.SearchResult
import com.example.photosearchproject.domain.repository.MainRepository
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val photosApiService: PhotosApiService) :
    MainRepository {
    override suspend fun getSearchData(
        query: String,
        page: Int
    ): Result<SearchResult> {
        return try {
            photosApiService
                .getPhotos(query, PAGE_SIZE, page)
                .decode()
                .map { responseModel ->
                    SearchResult(
                        photos = responseModel.photos.map { it.toPhoto() },
                        hasNextPage = responseModel.nextPage != null
                    )
                }
        } catch (exception: IOException) {
            Result.failure(exception)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}