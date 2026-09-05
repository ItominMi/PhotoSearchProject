package com.example.photosearchproject.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosearchproject.domain.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state: StateFlow<MainScreenState> get() = _state

    init {
        fetchData()
    }

    fun handleIntent(intent: MainScreenIntent) {
        when (intent) {
            is MainScreenIntent.Search -> {}
            is MainScreenIntent.FetchNext -> {}
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            mainUseCase.getSearchData("default", 1)
                .onSuccess { result ->

                }.onFailure { error -> }
        }
    }
}