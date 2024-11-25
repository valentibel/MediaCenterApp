package com.valentibel.mediacenterapp.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valentibel.medialibrary.model.MediaData
import com.valentibel.medialibrary.repository.MediaDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MediaDataState {
    data class Success(val data: MediaData): MediaDataState()
    data class Error(val message: String?): MediaDataState()
    data object Loading : MediaDataState()
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: MediaDataRepository): ViewModel() {
    private val _uiState = MutableStateFlow<MediaDataState>(MediaDataState.Loading)
    val uiState: StateFlow<MediaDataState> = _uiState

    private var loadJob: Job? = null

    init {
        loadMedia()
    }

    private fun loadMedia() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val result = repository.getMediaData()
            _uiState.value = if (result.isSuccess) {
                result.getOrNull()?.let {
                     MediaDataState.Success(data = it) }
                    ?: MediaDataState.Error(message = "Empty data")
            } else {
                MediaDataState.Error(message = result.exceptionOrNull()?.message)
            }
        }
    }
}