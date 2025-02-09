package com.valentibel.datalibrary.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ErrorResponse(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String
)