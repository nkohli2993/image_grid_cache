package com.rolling.meadows.data
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokenIDData(
    @SerializedName("token")
    var token: String = "",
    @SerializedName("user_id")
    var userId: String = ""
):Parcelable