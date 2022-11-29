package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RideCancelledData(
    @SerializedName("token")
    var token: String? =null,
    @SerializedName("ride_id")
    var rideId: Int? =null,
    @SerializedName("status")
    var status: Int? =null
): Parcelable