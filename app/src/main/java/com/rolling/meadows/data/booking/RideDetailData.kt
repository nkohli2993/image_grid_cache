package com.rolling.meadows.data.booking

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.authentication.UserProfileData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RideDetailData(
    @SerializedName("customer_details")
    var customerDetails: UserProfileData = UserProfileData(),
    @SerializedName("driver_details")
    var driverDetails: UserProfileData = UserProfileData(),
    @SerializedName("driver_id")
    var driverId: Int = 0,
    @SerializedName("drop_latitude")
    var dropLatitude: String = "",
    @SerializedName("drop_location")
    var dropLocation: String = "",
    @SerializedName("drop_longitude")
    var dropLongitude: String = "",
    @SerializedName("fare")
    var fare: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("is_favorite_driver")
    var isFavoriteDriver: Int = 0,
    @SerializedName("payment_status")
    var paymentStatus: Int = 0,
    @SerializedName("payment_method")
    var paymentMethod: Int = 0,
    @SerializedName("pickup_area")
    var pickupArea: String = "",
    @SerializedName("pickup_latitude")
    var pickupLatitude: String = "",
    @SerializedName("pickup_location")
    var pickupLocation: String = "",
    @SerializedName("pickup_longitude")
    var pickupLongitude: String = "",
    @SerializedName("ride_status")
    var rideStatus: Int = 0,
    @SerializedName("schedule_date")
    var scheduleDate: String = "",
    @SerializedName("schedule_date_time")
    var scheduleDateTime: String = "",
    @SerializedName("schedule_time")
    var scheduleTime: String = "",
    @SerializedName("total_distance")
    var totalDistance: Int = 0,
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("vehicle_id")
    var vehicleId: String = "",
    @SerializedName("ride_rating")
    var rideRating: RatingData? = null,
    @SerializedName("rider_rating")
    var riderRating: RatingData? = null
) : Parcelable