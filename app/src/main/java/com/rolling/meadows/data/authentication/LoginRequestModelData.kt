package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginRequestModelData(
    @SerializedName("device_type")
    var device_type: String? = null,
    @SerializedName("fcm_token")
    var fcmToken: String? = null,
    @SerializedName("device_id")
    var device_id: String? = null
) : Parcelable