package com.rolling.meadows.data.authentication


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginModel(
    @SerializedName("phone_code")
    var phoneCode: String? = null,
    @SerializedName("password")
//    var password: String = "12345678",
    var password: String? = null,
    @SerializedName("fcm_token")
    var fcmToken: String? = null,
    @SerializedName("device_type")
    var deviceType: Int? = null,
    @SerializedName("role")
    var role: String? = null,
    @SerializedName("iso_code")
    var isoCode:String?=null,
    @SerializedName("phone_number")
//    var phone_number: String = "0909090908"
    var phone_number:String?=null,


) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            phone_number.isNullOrEmpty() ->{
                errorPhoneNum = R.string.plz_enter_phone_number
            }
            phone_number!!.isNotEmpty() && phone_number!!.length<4 -> {
                errorPhoneNum = R.string.plz_enter_valid_phone_num
            }
            password.isNullOrEmpty() -> {
                errorPassword = R.string.plz_enter_password
            }
            password!!.length < 8 -> {
                errorPassword = R.string.validPass
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