package com.valentibel.medialibrary

import com.valentibel.medialibrary.api.MediaService
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.model.MediaItem
import com.valentibel.medialibrary.repository.MediaDataRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class MediaDataRepositoryTest {

    private lateinit var mediaService: MediaService
    private lateinit var repository: MediaDataRepository

    @Before
    fun setUp() {
        mediaService = mock(MediaService::class.java)
        repository = MediaDataRepository(mediaService)
    }

    @After
    fun tearDown() {
    }

   @Test
    fun getMediaData_success() = runTest {
        //Given
       val mockContent = Content(displayStyle = "List", items = listOf(
           MediaItem(id = "123", title = "Test title 1"),
           MediaItem(id = "456", title = "Test title 2"),
           MediaItem(id = "789", title = "Test title 3")
       ))
        `when`(mediaService.getMediaData()).thenReturn(mockContent)

        //When
        val result = repository.getMediaData()

        //Then
       assertTrue(result.isSuccess)
       assertEquals(mockContent, result.getOrNull())
       verify(mediaService, times(1)).getMediaData()
    }


    @Test
    fun getMediaData_failure() = runTest {
        //Given
        val errorMessage = "Error occurred!"
        `when`(mediaService.getMediaData()).thenThrow(MockitoException(errorMessage))

        //When
        val result = repository.getMediaData()

        //Then
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
        verify(mediaService, times(1)).getMediaData()
    }
}