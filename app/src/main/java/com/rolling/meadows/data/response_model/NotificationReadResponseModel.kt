package com.rolling.meadows.data.response_model

import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.events.EventDetailData

data class NotificationReadResponseModel(
    @SerializedName("data") var data: EventDetailData? = null,
) : BaseResponseModel()

