package com.valentibel.mediacenterapp.screens.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valentibel.datalibrary.model.BasicError
import com.valentibel.datalibrary.model.Result
import com.valentibel.mediacenterapp.components.MediaItemsView
import com.valentibel.mediacenterapp.data.MediaDataState
import com.valentibel.mediacenterapp.utils.Constants.DELIMITER
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.repository.MediaDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            _uiState.update {
                when(val result = repository.getMediaData()) {
                    is Result.Failure -> {
                        val message = when (result.error) {
                            is BasicError.ApiError -> "Api Error"
                            is BasicError.NetworkError -> "Network Error"
                            is BasicError.UnknownError -> "Please try again later!"
                        }
                        MediaDataState.Error(message = message)
                    }

                    is Result.Success -> MediaDataState.Success(data = result.data)
                }
            }
        }

    fun getContentByPath(path: String): Content? {
        return when (val state = _uiState.value) {
            is MediaDataState.Success -> {
                var content: Content? = state.data
                val items = path.split(DELIMITER)
                for (item in items) {
                    content = content?.items?.firstOrNull{ it.id == item && it.content != null}?.content
                }
                content
            }
            else -> null
        }
    }
}