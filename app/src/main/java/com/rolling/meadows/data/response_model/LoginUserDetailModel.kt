package com.rolling.meadows.data.response_model

import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.authentication.UserProfileData

data class LoginUserDetailModel(
    @SerializedName("data") var data: UserProfileData? = null,
) : BaseResponseModel()
