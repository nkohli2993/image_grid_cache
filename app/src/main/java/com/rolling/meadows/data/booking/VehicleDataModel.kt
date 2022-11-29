package com.rolling.meadows.data.booking
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleDataModel(
    @SerializedName("driver_available")
    var driverAvailable: Int = 0,
    @SerializedName("driver_id")
    var driverId: String = "",
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("per_km_fare")
    var perKmFare: Int = 0,
    @SerializedName("total_distance")
    var totalDistance: String = "",
    @SerializedName("total_fare")
    var totalFare: String = "N/A",
    @SerializedName("vehicle_base_fare")
    var vehicleBaseFare: Int = 0,
    @SerializedName("vehicle_id")
    var vehicleId: String = ""
):Parcelable