package com.app.imagerandom.domain.model

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : Response<T>()
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(message: String, data: T? = null) : Response<T>(data, message)

    fun dataOrNull(): T? {
        return if (this is Success) this.data else null
    }
}