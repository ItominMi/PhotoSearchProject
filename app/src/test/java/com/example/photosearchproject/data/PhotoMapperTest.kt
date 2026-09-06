package com.example.photosearchproject.data

import com.example.photosearchproject.data.model.PhotoResponseModel
import com.example.photosearchproject.data.model.PhotoSourceResponseModel
import com.example.photosearchproject.data.utils.toPhoto
import org.junit.Test
import org.junit.Assert.*

class PhotoMapperTest {

    private val mockPhotoResponseModel =
        PhotoResponseModel(
            id = 11L,
            url = "Mock Url",
            photographer = "Mock Photographer",
            src = PhotoSourceResponseModel(
                original = "Mock Original Url",
                medium = "Mock Medium Url",
                landscape = "Mock Landscape Url",
            )
        )

    @Test
    fun testPhotoMapperCorrectly() {
        assertEquals(11L, mockPhotoResponseModel.toPhoto().id)
        assertEquals("Mock Photographer", mockPhotoResponseModel.toPhoto().photographerName)
        assertEquals("Mock Medium Url", mockPhotoResponseModel.toPhoto().mediumUrl)
        assertEquals("Mock Original Url", mockPhotoResponseModel.toPhoto().originalUrl)
    }
}