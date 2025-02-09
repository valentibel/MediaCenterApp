package com.valentibel.mediacenterapp.screens.home

import com.valentibel.datalibrary.model.BasicError
import com.valentibel.datalibrary.model.Result
import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.mediacenterapp.utils.Constants.DELIMITER
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.model.DisplayStyle
import com.valentibel.medialibrary.model.MediaItem
import com.valentibel.medialibrary.repository.MediaDataRepository
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    private lateinit var viewModel: HomeScreenViewModel
    private val mockRepository: MediaDataRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(testDispatcher)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        clearMocks(mockRepository)
    }

    @Test
    fun `uiState emits Loading and then Success when repository returns data`() = runTest {
        // Given
        val mockContent = Content(DisplayStyle.GRID, emptyList())
        coEvery { mockRepository.getMediaData() } returns Result.Success(mockContent)

        // When
        viewModel = HomeScreenViewModel(mockRepository) // Trigger init block
        val initialState = viewModel.uiState.value
        advanceUntilIdle()
        val finalState = viewModel.uiState.value

        // Then
        assertEquals(MediaDataState.Loading, initialState)
        assertEquals(MediaDataState.Success(mockContent), finalState)
    }

    @Test
    fun `uiState emits Loading and then Error when repository returns ApiError`() = runTest {
        // Given
        coEvery { mockRepository.getMediaData() } returns Result.Failure(BasicError.ApiError("Error", 500))

        // When
        viewModel = HomeScreenViewModel(mockRepository)
        val initialState = viewModel.uiState.value
        advanceUntilIdle()
        val finalState = viewModel.uiState.value

        // Then
        assertEquals(MediaDataState.Loading, initialState)
        assertEquals(MediaDataState.Error("Api Error"), finalState)
    }

    @Test
    fun `uiState emits Loading and then Error when repository returns NetworkError`() = runTest {
        // Given
        coEvery { mockRepository.getMediaData() } returns Result.Failure(BasicError.NetworkError)

        // When
        viewModel = HomeScreenViewModel(mockRepository)
        val initialState = viewModel.uiState.value
        advanceUntilIdle()
        val finalState = viewModel.uiState.value

        // Then
        assertEquals(MediaDataState.Loading, initialState)
        assertEquals(MediaDataState.Error("Network Error"), finalState)
    }

    @Test
    fun `uiState emits Loading and then Error when repository returns UnknownError`() = runTest {
        // Given
        coEvery { mockRepository.getMediaData() } returns Result.Failure(BasicError.UnknownError(Exception("Unknown")))

        // When
        viewModel = HomeScreenViewModel(mockRepository)
        val initialState = viewModel.uiState.value
        advanceUntilIdle()
        val finalState = viewModel.uiState.value

        // Then
        assertEquals(MediaDataState.Loading, initialState)
        assertEquals(MediaDataState.Error("Please try again later!"), finalState)
    }

    @Test
    fun `getContentByPath returns correct content`() = runTest  {
        // Given
        val targetContent = Content(DisplayStyle.LIST, emptyList())
        val nestedItem = MediaItem(content = targetContent, id = "nested", title = "Nested")
        val rootContent = Content(DisplayStyle.GRID, listOf(MediaItem(id = "root", title = "Root", content = Content(DisplayStyle.LIST, listOf(nestedItem)))))
        coEvery { mockRepository.getMediaData() } returns Result.Success(rootContent)

        // When
        viewModel = HomeScreenViewModel(mockRepository)
        advanceUntilIdle()
        val finalState = viewModel.uiState.value
        val result = viewModel.getContentByPath("root${DELIMITER}nested")

        // Then
        assertEquals(MediaDataState.Success(rootContent), finalState)
        assertEquals(targetContent, result)
    }

    @Test
    fun `getContentByPath returns null if path is incorrect`() = runTest  {
        // Given
        val rootContent = Content(DisplayStyle.GRID, listOf(MediaItem(id = "root", title = "Root", content = Content(DisplayStyle.LIST, emptyList()))))
        coEvery { mockRepository.getMediaData() } returns Result.Success(rootContent)

        // When
        viewModel = HomeScreenViewModel(mockRepository)
        advanceUntilIdle()
        val finalState = viewModel.uiState.value
        val result = viewModel.getContentByPath("root${DELIMITER}invalid")

        // Then
        assertEquals(MediaDataState.Success(rootContent), finalState)
        assertNull(result)
    }
}