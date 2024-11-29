package com.valentibel.mediacenterapp.data

import com.valentibel.medialibrary.model.Content

sealed class MediaDataState {
    data class Success(val data: Content): MediaDataState()
    data class Error(val message: String?): MediaDataState()
    data object Loading : MediaDataState()
}