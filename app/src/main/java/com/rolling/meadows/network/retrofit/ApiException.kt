package com.rolling.meadows.network.retrofit

data class ApiException(
    var errorCode: Int? = 0,
    var errorMessage: String? = ""
) : Exception() {
    override val message: String?
        get() = errorMessage


}