package com.rolling.meadows.data.booking.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RatingRequest(
    var rating: Float? = null,
    var ride_id :Int? = null,
    var review:String= ""
): Parcelable

