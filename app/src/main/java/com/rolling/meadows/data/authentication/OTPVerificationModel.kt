package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import kotlinx.android.parcel.Parcelize


@Parcelize
data class OTPVerificationModel(
    @SerializedName("user_id")
    var user_id: String? = null,
    @SerializedName("otp")
    var otp: String? = null,
) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            otp.isNullOrEmpty() -> {
                errorPhoneNum = R.string.plz_enter_otp
            }
            (otp ?: "").isNotEmpty() && (otp ?: "").length < 4 -> {
                errorPhoneNum = R.string.plz_enter_valid_otp
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun reset() {
        errorPhoneNum = null
        errorPassword = null
    }
}