package com.example.photosearchproject.domain.repository

import com.example.photosearchproject.domain.model.SearchResult

interface MainRepository {
    suspend fun getSearchData(query: String, page: Int): Result<SearchResult>
}