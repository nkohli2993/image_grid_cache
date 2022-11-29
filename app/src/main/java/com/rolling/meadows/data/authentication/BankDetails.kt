package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BankDetails(
    @SerializedName("account_number")
    var accountNumber: String = "",
    @SerializedName("branch_name")
    var branchName: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("ifsc_code")
    var ifscCode: String = "",
    @SerializedName("last_name")
    var lastName: String = ""
):Parcelable