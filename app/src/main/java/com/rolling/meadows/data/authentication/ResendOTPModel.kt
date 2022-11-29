package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResendOTPModel (
    @SerializedName("user_id")
    var user_id: String? = null
):Parcelable