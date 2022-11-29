package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleDetails(
    @SerializedName("color")
    var color: String = "",
    @SerializedName("created_by")
    var createdBy: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image")
    var image: String = "",
    @SerializedName("insurance_expiry_date")
    var insuranceExpiryDate: String = "",
    @SerializedName("insurance_file")
    var insuranceFile: String = "",
    @SerializedName("insurance_issue_date")
    var insuranceIssueDate: String = "",
    @SerializedName("make")
    var make: String = "",
    @SerializedName("model")
    var model: String = "",
    @SerializedName("permit_file")
    var permitFile: String = "",
    @SerializedName("plate_number")
    var plateNumber: String = "",
    @SerializedName("registration_file")
    var registrationFile: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("vehicle_make")
    var vehicleMake: String = "",
    @SerializedName("vehicle_model")
    var vehicleModel: String = "",
    @SerializedName("vehicle_type")
    var vehicleType: String = "",
    @SerializedName("year")
    var year: String = ""
):Parcelable