package com.example.photosearchproject.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosearchproject.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
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
    private var paginationJob: Job? = null

    init {
        viewModelScope.launch {
            searchQueryState
                .debounce(DEBOUNCE_TIME)
                .collectLatest {
                    handleSearch(it)
                }
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
        val currentQuery = query.ifBlank { DEFAULT_QUERY }
        if (currentQuery == this.currentQuery && _state.value is MainScreenState.Success) return
        paginationJob?.cancelAndJoin()
        this.currentQuery = currentQuery
        currentPage = FIRST_PAGE
        _state.value = MainScreenState.Loading
        fetchData(page = currentPage, query = currentQuery)
    }

    private fun handleFetchNextPage() {
        val currentState = _state.value as? MainScreenState.Success ?: return
        if (currentState.hasReachedLastPage) return
        if (paginationJob?.isActive == true) return
        val nextPage = currentPage + 1
        val query = currentQuery
        paginationJob = viewModelScope.launch {
            fetchData(page = nextPage, query = query)
        }
    }

    private suspend fun fetchData(page: Int, query: String) {
        val result = mainUseCase.getSearchData(query, page)

        currentCoroutineContext().ensureActive()
        if (query != currentQuery) return

        result.onSuccess { result ->
            currentPage = page
            _state.update { currentState ->
                when {
                    page > FIRST_PAGE && currentState is MainScreenState.Success -> currentState.copy(
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
            _state.value = MainScreenState.Error(error.message ?: "API Error")
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val DEFAULT_QUERY = "nature"
        private const val DEBOUNCE_TIME = 400L
    }
}