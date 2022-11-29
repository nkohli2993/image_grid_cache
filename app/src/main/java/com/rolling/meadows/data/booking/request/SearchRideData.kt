package com.rolling.meadows.data.booking.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRideData(
    @SerializedName("pickup_location")
    var pickup_location: String? = null,
    @SerializedName("pickup_area")
    var pickup_area: String? = null,
    @SerializedName("drop_location")
    var drop_location: String? = null,
    @SerializedName("pickup_latitude")
    var pickup_latitude: String? = null,
    @SerializedName("pickup_longitude")
    var pickup_longitude: String? = null,
    @SerializedName("drop_latitude")
    var drop_latitude: String? = null,
    @SerializedName("drop_longitude")
    var drop_longitude: String? = null
    ) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            pickup_location.isNullOrEmpty() -> {
                errorPickUpLocation = R.string.plz_enter_full_name
            }
            drop_location.isNullOrEmpty() -> {
                errorDropOffLocation = R.string.plz_enter_last_name
            }

            else -> {
                return true
            }
        }
        return false
    }

    private fun reset() {
        errorPickUpLocation = null
        errorDropOffLocation = null

    }
}