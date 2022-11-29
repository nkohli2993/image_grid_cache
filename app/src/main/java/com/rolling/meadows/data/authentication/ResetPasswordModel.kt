package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResetPasswordModel(
    @SerializedName("user_id")
    var userId: String? = null,
    @SerializedName("reset_password_otp")
    var resetPasswordOtp: String? = null,
    @SerializedName("new_password")
    var newPassword: String? = null,
    @SerializedName("confirm_new_password")
    var confirmNewPassword: String? = null,
) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            newPassword.isNullOrEmpty() -> {
                errorPassword = R.string.plz_enter_new_password
            }
            confirmNewPassword.isNullOrEmpty() -> {
                errorConfirmPassword = R.string.plz_enter_confirm_password
            }
            newPassword.toString() != confirmNewPassword.toString() ->{
                errorConfirmPassword = R.string.new_password_doesnot_matched_with_old_password
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun reset() {
        errormessage = null
        errorPassword = null
        errorConfirmPassword = null
    }
}