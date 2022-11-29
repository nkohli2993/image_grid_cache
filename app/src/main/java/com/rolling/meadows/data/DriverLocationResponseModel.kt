package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.response_model.BaseResponseModel
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DriverLocationResponseModel (
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("estimate_time")
    var estimateTime: String = "",
    @SerializedName("estimate_distance")
    var estimateDistance: String = "",
): BaseResponseModel(), Parcelable

