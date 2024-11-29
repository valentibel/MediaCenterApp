package com.valentibel.mediacenterapp.screens.details

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
class DetailsScreenViewModel @Inject constructor(private val repository: MediaDataRepository): ViewModel()  {
    private val _uiState = MutableStateFlow<MediaDataState>(MediaDataState.Loading)
    val uiState: StateFlow<MediaDataState> = _uiState.asStateFlow()

    fun loadMedia(path: String) =
        viewModelScope.launch {
            val result = repository.getMediaData()
            _uiState.value = if (result.isSuccess) {
                result.getOrNull()?.let {
                    getContentByPath(path, it)
                }
                    ?: MediaDataState.Error(message = "Empty data")
            } else {
                MediaDataState.Error(message = result.exceptionOrNull()?.message)
            }
        }

    private fun getContentByPath(path: String, mediaData: Content): MediaDataState {
        var content: Content? = mediaData
        val items = path.split(DELIMITER)
        for (item in items) {
            content = content?.items?.firstOrNull{ it.id == item && it.content != null}?.content
        }
        return if (content != null) MediaDataState.Success(data = content)
        else MediaDataState.Error(message = "Path not found: $path")
    }
}