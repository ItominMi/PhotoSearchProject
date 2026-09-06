package com.example.photosearchproject.presentation.mainscreen

import com.example.photosearchproject.domain.model.SearchResult
import com.example.photosearchproject.domain.usecase.MainUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mainUseCase: MainUseCase = mockk()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() {
        createViewModel()

        assertEquals(MainScreenState.Loading, viewModel.state.value)
    }

    @Test
    fun testSuccessResponse() = runTest(testDispatcher) {
        mockSuccessResponse()
        createViewModel()

        viewModel.handleIntent(MainScreenIntent.Search("Mock"))
        advanceTimeBy(401L)
        advanceUntilIdle()

        assertEquals(
            MainScreenState.Success(photos = listOf(), hasReachedLastPage = false),
            viewModel.state.value
        )
    }

    @Test
    fun testErrorResponse() = runTest(testDispatcher) {
        mockErrorResponse()
        createViewModel()

        viewModel.handleIntent(MainScreenIntent.Search("Mock"))
        advanceTimeBy(401L)
        advanceUntilIdle()

        assertEquals(
            MainScreenState.Error("Mock Error"),
            viewModel.state.value
        )
    }

    private fun mockSuccessResponse() {
        coEvery { mainUseCase.getSearchData(any(), any()) } answers {
            Result.success(SearchResult(photos = listOf(), hasNextPage = true))
        }
    }

    private fun mockErrorResponse() {
        coEvery { mainUseCase.getSearchData(any(), any()) } answers {
            Result.failure(Exception("Mock Error"))
        }
    }

    private fun createViewModel() {
        viewModel = MainViewModel(mainUseCase)
    }
}