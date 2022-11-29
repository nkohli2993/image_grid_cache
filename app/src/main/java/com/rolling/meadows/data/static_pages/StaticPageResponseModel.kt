package com.rolling.meadows.data.static_pages

import com.rolling.meadows.data.response_model.BaseResponseModel
import com.google.gson.annotations.SerializedName


data class StaticPageResponseModel(
    @SerializedName("data") var data: StaticPageModel? = null,
) : BaseResponseModel()

