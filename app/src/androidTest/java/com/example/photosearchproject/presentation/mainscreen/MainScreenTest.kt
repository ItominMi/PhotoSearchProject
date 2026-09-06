package com.example.photosearchproject.presentation.mainscreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.photosearchproject.domain.model.Photo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testLoadingState_ShowsLoadingIndicator() {
        composeTestRule.setContent {
            MainScreenContent(
                screenState = MainScreenState.Loading,
                onPhotoClick = {},
                onSearchChanged = {},
                onFetchNext = {}
            )
        }

        composeTestRule.onNodeWithTag(MAIN_SCREEN_LOADING_INDICATOR_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(MAIN_SCREEN_PHOTO_LIST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MAIN_SCREEN_ERROR_TAG).assertDoesNotExist()
    }

    @Test
    fun testErrorState_ShowsErrorScreen() {
        composeTestRule.setContent {
            MainScreenContent(
                screenState = MainScreenState.Error("Mock Error message"),
                onPhotoClick = {},
                onSearchChanged = {},
                onFetchNext = {}
            )
        }

        composeTestRule.onNodeWithTag(MAIN_SCREEN_LOADING_INDICATOR_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MAIN_SCREEN_PHOTO_LIST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MAIN_SCREEN_ERROR_TAG).assertIsDisplayed()
    }

    @Test
    fun testSuccessState_ShowsPhotoListScreen() {
        composeTestRule.setContent {
            MainScreenContent(
                screenState = MainScreenState.Success(
                    photos = listOf(
                        Photo(
                            id = 11L,
                            photographerName = "Mock",
                            originalUrl = "Mock",
                            mediumUrl = "Mock"
                        ),
                        Photo(
                            id = 12L,
                            photographerName = "Mock",
                            originalUrl = "Mock",
                            mediumUrl = "Mock"
                        ),
                        Photo(
                            id = 13L,
                            photographerName = "Mock",
                            originalUrl = "Mock",
                            mediumUrl = "Mock"
                        )
                    )
                ),
                onPhotoClick = {},
                onSearchChanged = {},
                onFetchNext = {}
            )
        }

        composeTestRule.onNodeWithTag(MAIN_SCREEN_LOADING_INDICATOR_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MAIN_SCREEN_PHOTO_LIST_TAG).assertIsDisplayed()
        assertEquals(3, composeTestRule.onAllNodesWithTag(MAIN_SCREEN_ITEM_TAG).fetchSemanticsNodes().size)
        composeTestRule.onNodeWithTag(MAIN_SCREEN_ERROR_TAG).assertDoesNotExist()
    }

    @Test
    fun testPhotoClickState_CallNavigationFunction() {
        var clicked = false
        composeTestRule.setContent {
            MainScreenContent(
                screenState = MainScreenState.Success(
                    photos = listOf(
                        Photo(
                            id = 11L,
                            photographerName = "Mock",
                            originalUrl = "Mock",
                            mediumUrl = "Mock"
                        )
                    )
                ),
                onPhotoClick = {
                    clicked = true
                },
                onSearchChanged = {},
                onFetchNext = {}
            )
        }

        composeTestRule.onNodeWithTag(MAIN_SCREEN_LOADING_INDICATOR_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MAIN_SCREEN_PHOTO_LIST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(MAIN_SCREEN_ERROR_TAG).assertDoesNotExist()

        composeTestRule.onNodeWithTag(MAIN_SCREEN_ITEM_TAG).performClick()

        assertEquals(true, clicked)
    }
}