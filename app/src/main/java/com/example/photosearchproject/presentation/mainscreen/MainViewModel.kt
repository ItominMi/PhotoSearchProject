package com.example.photosearchproject.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosearchproject.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state: StateFlow<MainScreenState> get() = _state

    private val searchQueryState = MutableStateFlow(DEFAULT_QUERY)

    private var currentPage: Int = FIRST_PAGE
    private var currentQuery: String = DEFAULT_QUERY
    private var isLoading: Boolean = false

    init {
        viewModelScope.launch {
            searchQueryState
                .drop(1)
                .debounce(DEBOUNCE_TIME)
                .collectLatest {
                    handleSearch(it)
                }
        }
        viewModelScope.launch {
            fetchData()
        }
    }

    fun handleIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.Search -> {
                searchQueryState.value = intent.query
            }

            is MainScreenIntent.FetchNext -> {
                handleFetchNextPage()
            }
        }
    }

    private suspend fun handleSearch(query: String) {
        val str = query.ifBlank { DEFAULT_QUERY }
        if (str == currentQuery && _state.value is MainScreenState.Success) return
        currentQuery = str
        currentPage = FIRST_PAGE
        _state.value = MainScreenState.Loading
        fetchData()
    }

    private fun handleFetchNextPage() {
        if (isLoading) return
        val currentState = _state.value as MainScreenState.Success
        if (currentState.hasReachedLastPage) return
        currentPage++
        viewModelScope.launch {
            fetchData()
        }
    }

    private suspend fun fetchData() {
        if (isLoading) return
        isLoading = true
        mainUseCase.getSearchData(currentQuery, currentPage)
            .onSuccess { result ->
                isLoading = false
                _state.update { currentState ->
                    when (currentState) {
                        is MainScreenState.Success -> currentState.copy(
                            photos = (currentState.photos + result.photos).distinctBy { it.id },
                            hasReachedLastPage = !result.hasNextPage
                        )

                        else -> MainScreenState.Success(
                            photos = result.photos,
                            hasReachedLastPage = !result.hasNextPage
                        )
                    }
                }
            }.onFailure { error ->
                isLoading = false
                _state.value = MainScreenState.Error(error.message ?: "API Error")
            }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val DEFAULT_QUERY = "nature"
        private const val DEBOUNCE_TIME = 400L
    }
}