package com.valentibel.datalibrary.model

sealed class BasicError {
    data class ApiError(val message: String?, val code: Int) : BasicError()
    data object NetworkError : BasicError()
    data class UnknownError(val error: Throwable) : BasicError()
}
