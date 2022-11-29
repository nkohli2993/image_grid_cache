package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForgotPasswordModel(
    @SerializedName("phone_code")
    var phoneCode: String? = null,
    @SerializedName("phone_number")
    var phoneNumber: String? = null
) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            phoneCode.isNullOrEmpty()->{
                errorPhoneCode = R.string.plz_select_phone_code
            }
            phoneNumber.isNullOrEmpty() -> {
                errorPhoneNum = R.string.plz_enter_phone_number
            }
            phoneNumber!!.isNotEmpty() && phoneNumber!!.length<6 -> {
                errorPhoneNum = R.string.plz_enter_valid_phone_num
            }

            else -> {
                return true
            }
        }
        return false
    }

    private fun reset() {
        errorPhoneCode = null
        errorPhoneNum = null
    }
}