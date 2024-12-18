package com.valentibel.medialibrary.repository

import com.valentibel.medialibrary.api.MediaService
import com.valentibel.medialibrary.model.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaDataRepository @Inject constructor(private val service: MediaService) {
    suspend fun getMediaData() : Result<Content> = withContext(Dispatchers.IO) {
        try {
            val data = service.getMediaData()
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}