package com.valentibel.medialibrary

import com.valentibel.datalibrary.model.BasicError
import com.valentibel.datalibrary.model.Result
import com.valentibel.datalibrary.network.ErrorResponse
import com.valentibel.datalibrary.network.adapter.NetworkResponse
import com.valentibel.medialibrary.api.MediaService
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.model.DisplayStyle
import com.valentibel.medialibrary.repository.MediaDataRepositoryImpl
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MediaDataRepositoryTest {

    private lateinit var repository: MediaDataRepositoryImpl
    private val mockMediaService: MediaService = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = MediaDataRepositoryImpl(
            service = mockMediaService,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        clearMocks(mockMediaService)
    }

    @Test
    fun `getDataItems returns Success when NetworkResponse is Success and data is valid`() = runTest(testDispatcher) {
        // Given
        val mockContent = Content(DisplayStyle.GRID, emptyList())
        coEvery { mockMediaService.getMediaData() } returns NetworkResponse.Success(mockContent)

        // When
        val result = repository.getMediaData()

        // Then
        assert(result is Result.Success)
        assertEquals(mockContent, (result as Result.Success).data)
    }

    @Test
    fun `getDataItems returns Success when NetworkResponse is Success and data is null`() = runTest(testDispatcher) {
        // Given
        coEvery { mockMediaService.getMediaData() } returns NetworkResponse.Success(null)

        // When
        val result = repository.getMediaData()

        // Then
        assert(result is Result.Failure && result.error is BasicError.UnknownError)
    }

    @Test
    fun `getDataItems returns Failure when NetworkResponse is ApiError`() = runTest(testDispatcher) {
        // Given
        val errorCode = 404
        val errorMessage = "Not Found"
        val errorResponse = ErrorResponse(errorCode, errorMessage)
        coEvery { mockMediaService.getMediaData() } returns NetworkResponse.ApiError(errorResponse, errorCode)

        // When
        val result = repository.getMediaData()

        // Then
        assert(result is Result.Failure && result.error is BasicError.ApiError)
        val apiError = (result as Result.Failure).error as BasicError.ApiError
        assertEquals(errorMessage, apiError.message)
        assertEquals(errorCode, apiError.code)
    }

    @Test
    fun `getDataItems returns Failure when NetworkResponse is NetworkError`() = runTest(testDispatcher) {
        // Given
        coEvery { mockMediaService.getMediaData() } returns NetworkResponse.NetworkError(IOException())

        // When
        val result = repository.getMediaData()

        // Then
        assert(result is Result.Failure && result.error is BasicError.NetworkError)
    }

    @Test
    fun `getDataItems returns Failure when NetworkResponse is UnknownError`() = runTest(testDispatcher) {
        // Given
        val exception = RuntimeException("Something went wrong")
        coEvery { mockMediaService.getMediaData() } returns NetworkResponse.UnknownError(exception)

        // When
        val result = repository.getMediaData()

        // Then
        assert(result is Result.Failure && result.error is BasicError.UnknownError)
        val unknownError = (result as Result.Failure).error as BasicError.UnknownError
        assertEquals(exception, unknownError.error)
    }
}