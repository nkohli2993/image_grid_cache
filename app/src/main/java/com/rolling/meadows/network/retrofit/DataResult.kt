package com.rolling.meadows.network.retrofit


sealed class DataResult<out T> {
    data class Loading(val nothing: Nothing? = null) : DataResult<Nothing>()
    data class Error(val errorInt: Int? = null, val errorString: String? = null) :
        DataResult<Nothing>()

    data class Success<out T>(val data: T? = null, var type: Int? = null) : DataResult<T>()
    data class Failure(
        val message: String? = null,
        val exception: Exception? = null,
        val errorCode: Int? = null,
        val errorBody: String? = null
    ) :
        DataResult<Nothing>()
}