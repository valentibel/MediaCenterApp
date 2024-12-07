package com.valentibel.mediacenterapp.screens.details

import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.mediacenterapp.screens.home.HomeScreenViewModel
import com.valentibel.mediacenterapp.utils.Constants.DELIMITER
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.model.MediaItem
import com.valentibel.medialibrary.repository.MediaDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsScreenViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DetailsScreenViewModel
    private lateinit var mockRepository: MediaDataRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockRepository = mock(MediaDataRepository::class.java)
        viewModel = DetailsScreenViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMedia success loading and valid path`() = runTest {
        //Given
        val validPath = "333${DELIMITER}444"
        val mockContent = Content(displayStyle = "List", items = listOf(
            MediaItem(id = "123", title = "Test title 1"),
            MediaItem(id = "456", title = "Test title 2"),
            MediaItem(id = "789", title = "Test title 3")
        ))
        val mockRootContent = Content(displayStyle = "Grid", items = listOf(
            MediaItem(id = "111", title = "Root item 1"),
            MediaItem(id = "222", title = "Root item 2"),
            MediaItem(id = "333", title = "Root item 3",  content = Content(displayStyle = "List", items = listOf(
                MediaItem(id = "444", title = "Test title 1", content = mockContent)
            )))
        ))
        `when`(mockRepository.getMediaData()).thenReturn(Result.success(mockRootContent))

        //When
        viewModel.loadMedia(validPath)

        //Then
        // Assert initial loading state
        assertEquals(MediaDataState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert successful state
        assertEquals(MediaDataState.Success(mockContent), viewModel.uiState.first())
        verify(mockRepository, times(1)).getMediaData()
    }

    @Test
    fun `loadMedia success loading and invalid path`() = runTest {
        //Given
        val invalidPath = "111${DELIMITER}444"
        val mockContent = Content(displayStyle = "List", items = listOf(
            MediaItem(id = "123", title = "Test title 1"),
            MediaItem(id = "456", title = "Test title 2"),
            MediaItem(id = "789", title = "Test title 3")
        ))
        val mockRootContent = Content(displayStyle = "Grid", items = listOf(
            MediaItem(id = "111", title = "Root item 1"),
            MediaItem(id = "222", title = "Root item 2"),
            MediaItem(id = "333", title = "Root item 3",  content = Content(displayStyle = "List", items = listOf(
                MediaItem(id = "444", title = "Test title 1", content = mockContent)
            )))
        ))
        `when`(mockRepository.getMediaData()).thenReturn(Result.success(mockRootContent))

        //When
        viewModel.loadMedia(invalidPath)

        //Then
        // Assert initial loading state
        assertEquals(MediaDataState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert successful state
        assert(viewModel.uiState.first() is MediaDataState.Error)
        verify(mockRepository, times(1)).getMediaData()
    }

    @Test
    fun `loadMedia loading data fails`() = runTest {
        //Given
        val validPath = "333${DELIMITER}444"
        val mockContent = Content(displayStyle = "List", items = listOf(
            MediaItem(id = "123", title = "Test title 1"),
            MediaItem(id = "456", title = "Test title 2"),
            MediaItem(id = "789", title = "Test title 3")
        ))
        val mockRootContent = Content(displayStyle = "Grid", items = listOf(
            MediaItem(id = "111", title = "Root item 1"),
            MediaItem(id = "222", title = "Root item 2"),
            MediaItem(id = "333", title = "Root item 3",  content = Content(displayStyle = "List", items = listOf(
                MediaItem(id = "444", title = "Test title 1", content = mockContent)
            )))
        ))
        `when`(mockRepository.getMediaData()).thenReturn(Result.failure(MockitoException("Error occurred")))

        //When
        viewModel.loadMedia(validPath)

        //Then
        // Assert initial loading state
        assertEquals(MediaDataState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert successful state
        assert(viewModel.uiState.first() is MediaDataState.Error)
        verify(mockRepository, times(1)).getMediaData()
    }
}