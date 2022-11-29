package com.rolling.meadows.data

import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.response_model.BaseResponseModel

data class FavoriteResponseModel(
    @SerializedName("data") var data: FavoriteDataModel? = null,
) : BaseResponseModel()

