package com.valentibel.mediacenterapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.mediacenterapp.utils.Constants.DELIMITER
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.repository.MediaDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: MediaDataRepository): ViewModel() {
    private val _uiState = MutableStateFlow<MediaDataState>(MediaDataState.Loading)
    val uiState: StateFlow<MediaDataState> = _uiState.asStateFlow()

    init {
        loadMedia()
    }

    private fun loadMedia() =
        viewModelScope.launch {
            val result = repository.getMediaData()
            _uiState.value = if (result.isSuccess) {
                result.getOrNull()?.let { data ->
                    MediaDataState.Success(data = data)
                }
                    ?: MediaDataState.Error(message = "Empty data")
            } else {
                MediaDataState.Error(message = result.exceptionOrNull()?.message)
            }
        }

    fun getContentByPath(path: String, mediaData: Content): Content? {
        var content: Content? = mediaData
        val items = path.split(DELIMITER)
        for (item in items) {
            content = content?.items?.firstOrNull{ it.id == item && it.content != null}?.content
        }
        return content
    }
}