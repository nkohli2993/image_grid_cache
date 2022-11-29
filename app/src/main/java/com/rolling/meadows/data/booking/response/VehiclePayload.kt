package com.rolling.meadows.data.booking.response

import com.google.gson.annotations.SerializedName

data class VehiclePayload(

    @SerializedName("statusCode") var statusCode: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: VehicleResponseModel? = null,

    )