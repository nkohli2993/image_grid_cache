package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DrivingLicense(
    @SerializedName("license_expiry_date")
    var licenseExpiryDate: String = "",
    @SerializedName("license_file")
    var licenseFile: String = "",
    @SerializedName("license_issue_date")
    var licenseIssueDate: String = "",
    @SerializedName("license_number")
    var licenseNumber: String = ""
) : Parcelable