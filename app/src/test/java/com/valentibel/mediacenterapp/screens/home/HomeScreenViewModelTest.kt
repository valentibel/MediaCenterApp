package com.valentibel.mediacenterapp.screens.home

import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.mediacenterapp.utils.Constants.DELIMITER
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.model.DisplayStyle
import com.valentibel.medialibrary.model.MediaItem
import com.valentibel.medialibrary.repository.MediaDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var mockRepository: MediaDataRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockRepository = mock(MediaDataRepository::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial MediaDataState is Loading and then success`() = runTest {
        //Given
        val mockContent = Content(displayStyle = DisplayStyle.LIST, items = listOf(
            MediaItem(id = "123", title = "Test title 1"),
            MediaItem(id = "456", title = "Test title 2"),
            MediaItem(id = "789", title = "Test title 3")
        ))
        `when`(mockRepository.getMediaData()).thenReturn(Result.success(mockContent))

        //When
        viewModel = HomeScreenViewModel(mockRepository)

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
    fun `initial MediaDataState is Loading and then failure`() = runTest {
        //Given
        val errorMessage = "Error occurred!"
        `when`(mockRepository.getMediaData()).thenReturn(Result.failure(MockitoException(errorMessage)))

        //When
        viewModel = HomeScreenViewModel(mockRepository)

        //Then
        // Assert initial loading state
        assertEquals(MediaDataState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert error state
        assertEquals(MediaDataState.Error(errorMessage), viewModel.uiState.first())
        verify(mockRepository, times(1)).getMediaData()
    }

    @Test
    fun `getContentByPath success`() = runTest {
        //Given
        val validPath = "333${DELIMITER}444"
        val mockContent = Content(displayStyle = DisplayStyle.LIST, items = listOf(
            MediaItem(id = "123", title = "Test title 1"),
            MediaItem(id = "456", title = "Test title 2"),
            MediaItem(id = "789", title = "Test title 3")
        ))
        val mockRootContent = Content(displayStyle = DisplayStyle.GRID, items = listOf(
            MediaItem(id = "111", title = "Root item 1"),
            MediaItem(id = "222", title = "Root item 2"),
            MediaItem(id = "333", title = "Root item 3",  content = Content(displayStyle = DisplayStyle.LIST, items = listOf(
                MediaItem(id = "444", title = "Test title 1", content = mockContent)
            )))
        ))
        `when`(mockRepository.getMediaData()).thenReturn(Result.success(mockRootContent))

        //When
        viewModel = HomeScreenViewModel(mockRepository)

        //Then
        // Assert initial loading state
        assertEquals(MediaDataState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert successful state
        assertEquals(MediaDataState.Success(mockRootContent), viewModel.uiState.first())
        verify(mockRepository, times(1)).getMediaData()

        //When
        val detailsData = viewModel.getContentByPath(
            path = validPath,
            mediaData = (viewModel.uiState.first() as MediaDataState.Success).data)

        assertEquals(mockContent, detailsData)
        verify(mockRepository, times(1)).getMediaData()
    }

    @Test
    fun `getContentByPath failure`() = runTest {
        //Given
        val invalidPath = "333${DELIMITER}444"
        val mockContent = Content(displayStyle = DisplayStyle.LIST, items = listOf(
            MediaItem(id = "123", title = "Test title 1"),
            MediaItem(id = "456", title = "Test title 2"),
            MediaItem(id = "789", title = "Test title 3")
        ))
        `when`(mockRepository.getMediaData()).thenReturn(Result.success(mockContent))

        //When
        viewModel = HomeScreenViewModel(mockRepository)

        //Then
        // Assert initial loading state
        assertEquals(MediaDataState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert successful state
        assertEquals(MediaDataState.Success(mockContent), viewModel.uiState.first())
        verify(mockRepository, times(1)).getMediaData()

        //When
        val detailsData = viewModel.getContentByPath(
            path = invalidPath,
            mediaData = (viewModel.uiState.first() as MediaDataState.Success).data)

        assertNull(detailsData)
        verify(mockRepository, times(1)).getMediaData()
    }
}