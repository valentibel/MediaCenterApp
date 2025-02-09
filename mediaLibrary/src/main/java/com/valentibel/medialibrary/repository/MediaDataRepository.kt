package com.valentibel.medialibrary.repository

import com.valentibel.datalibrary.model.BasicError
import com.valentibel.datalibrary.model.Result
import com.valentibel.datalibrary.network.adapter.NetworkResponse
import com.valentibel.medialibrary.api.MediaService
import com.valentibel.medialibrary.di.MediaServiceLocal
import com.valentibel.medialibrary.di.MediaServiceNetwork
import com.valentibel.medialibrary.model.Content
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MediaDataRepository {
    suspend fun getMediaData() : Result<Content, BasicError>
}

class MediaDataRepositoryImpl @Inject constructor(
    @MediaServiceLocal private val service: MediaService,
//    @MediaServiceNetwork private val service: MediaService,
    private val ioDispatcher: CoroutineDispatcher
): MediaDataRepository {

    override suspend fun getMediaData() : Result<Content, BasicError> = withContext(ioDispatcher) {
        when (val result = service.getMediaData()) {

            is NetworkResponse.ApiError -> {
                val errorResponse = result.body
                Result.Failure(
                    BasicError.ApiError(
                        errorResponse.statusMessage,
                        errorResponse.statusCode
                    )
                )
            }

            is NetworkResponse.NetworkError -> {
                Result.Failure(BasicError.NetworkError)
            }

            is NetworkResponse.Success -> {
                val rawData = result.body
                if (rawData == null) {
                    Result.Failure(BasicError.UnknownError(Throwable("Data is null")))
                } else {
                    Result.Success(rawData)
                }
            }

            is NetworkResponse.UnknownError -> {
                Result.Failure(BasicError.UnknownError(result.error))
            }
        }
    }
}