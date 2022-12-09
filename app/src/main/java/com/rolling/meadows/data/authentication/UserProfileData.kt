package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserProfileData(
    @SerializedName("auth_token")
    var authToken: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("email_notification")
    var emailNotification: String = "",
    @SerializedName("email_verification_otp")
    var emailVerificationOtp: String = "",
    @SerializedName("email_verified_at")
    var emailVerifiedAt: String = "",
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("notification")
    var notification: Int = 0,
    @SerializedName("profile_image")
    var profileImage: String = "",
    @SerializedName("role")
    var role: Int = 0,
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = ""
):Parcelable