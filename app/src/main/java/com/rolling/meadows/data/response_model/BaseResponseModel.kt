package com.rolling.meadows.data.response_model

import com.google.gson.annotations.SerializedName

open class BaseResponseModel{
    @SerializedName("message")
    open var message: String? = null
    @SerializedName("statusCode")
    var statusCode: String? = null
    @SerializedName("status")
    var status: String? = null
}

