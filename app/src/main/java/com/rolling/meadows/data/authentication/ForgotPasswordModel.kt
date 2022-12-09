package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import com.rolling.meadows.utils.extensions.isValidEmail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForgotPasswordModel(
    @SerializedName("email")
    var email: String? = null,

) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            email.isNullOrEmpty() -> {
                errorEmail = R.string.plz_enter_email_address
            }
            (email!!.trim()).isNotEmpty() && !email!!.trim().isValidEmail() -> {
                errorEmail = R.string.plz_enter_valid_email_address
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun reset() {
        errorEmail = null
    }
}