package com.valentibel.medialibrary.api

import android.content.Context
import com.valentibel.datalibrary.network.ErrorResponse
import com.valentibel.datalibrary.network.adapter.NetworkResponse
import com.valentibel.medialibrary.model.Content
import com.valentibel.medialibrary.util.Constants.MOCK_API_RESPONSE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

interface MediaService {
    suspend fun getMediaData(): NetworkResponse<Content, ErrorResponse>
}

class MediaServiceMockImpl @Inject constructor(@ApplicationContext private val appContext: Context): MediaService {

    override suspend fun getMediaData(): NetworkResponse<Content, ErrorResponse> =
        try {
            val stringData = readAssetFile(MOCK_API_RESPONSE, appContext)
            val result = Json.decodeFromString<Content>(stringData)
            NetworkResponse.Success(result)
        } catch (e: Exception) {
            val message = when (e) {
                is IOException -> "File $MOCK_API_RESPONSE not found"
                is IllegalArgumentException -> "JSON is malformed"
                else -> e.message
            }
            e.printStackTrace()
            NetworkResponse.UnknownError(Throwable(message))
        }

    @Throws(IOException::class)
    private fun readAssetFile(fileName: String, context: Context): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }

}