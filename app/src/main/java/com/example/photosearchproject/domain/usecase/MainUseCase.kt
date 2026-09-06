package com.example.photosearchproject.domain.usecase

import com.example.photosearchproject.domain.model.SearchResult
import com.example.photosearchproject.domain.repository.MainRepository
import javax.inject.Inject

class MainUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun getSearchData(query: String, page: Int): Result<SearchResult> {
        return mainRepository.getSearchData(query, page)
    }
}