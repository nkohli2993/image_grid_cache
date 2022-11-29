package com.rolling.meadows.data.booking.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RideRequest(
    var pickup_location: String? = null,
    var drop_location: String? = null,
    var pickup_area: String? = null,
    var pickup_latitude: String? = null,
    var pickup_longitude: String? = null,
    var drop_latitude: String? = null,
    var drop_longitude: String? = null,
    var driver_id: String? = null,
    var vehicle_id: String? = null,
    var payment_method: Int? = null,
) : Parcelable
