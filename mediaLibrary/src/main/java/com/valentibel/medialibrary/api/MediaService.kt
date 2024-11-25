package com.valentibel.medialibrary.api

import android.content.Context
import com.valentibel.medialibrary.model.MediaData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface MediaService {
    suspend fun getMediaData(): MediaData
}

class MediaServiceImpl @Inject constructor(@ApplicationContext private val appContext: Context): MediaService {

    override suspend fun getMediaData(): MediaData {
        val fileName = "media.json"
        val stringData = readAssetFile(fileName, appContext)
        return Json.decodeFromString<MediaData>(stringData)
    }

    private fun readAssetFile(fileName: String, context: Context): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }

}