package com.butlerschocolates.app.api

import okhttp3.ResponseBody

sealed class ApiHandler<out T> {

    data class Success<out T>(val value: T) : ApiHandler<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : ApiHandler<Nothing>()

    object Loading : ApiHandler<Nothing>()

    data class Error<out T>(
       val throwable: Throwable, val data: T?
    ) : ApiHandler<T>()
}