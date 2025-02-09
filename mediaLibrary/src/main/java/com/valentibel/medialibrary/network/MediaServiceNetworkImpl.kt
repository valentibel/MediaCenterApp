package com.valentibel.medialibrary.network

import com.valentibel.datalibrary.network.ErrorResponse
import com.valentibel.datalibrary.network.adapter.NetworkResponse
import com.valentibel.medialibrary.api.MediaService
import com.valentibel.medialibrary.model.Content
import retrofit2.http.GET

internal interface MediaServiceNetworkImpl: MediaService {

    @GET("media.json")
    override suspend fun getMediaData(): NetworkResponse<Content, ErrorResponse>
}