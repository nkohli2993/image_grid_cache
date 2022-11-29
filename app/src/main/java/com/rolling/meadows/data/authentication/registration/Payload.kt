package com.rolling.meadows.data.authentication.registration

import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.authentication.UserProfileData

data class Payload(

    @SerializedName("statusCode") var statusCode: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var token: UserProfileData? = null,

    )