package com.rolling.meadows.data.response_model

import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.NotificationDataModel

data class NotificationResponseModel(
    @SerializedName("data") var data: NotificationDataModel? = null,
) : BaseResponseModel()

