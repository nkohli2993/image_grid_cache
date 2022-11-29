package com.rolling.meadows.data.booking.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CancelRideData(
    var ride_id: Int? = null,
):Parcelable

