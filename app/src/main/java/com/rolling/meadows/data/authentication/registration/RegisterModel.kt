package com.rolling.meadows.data.authentication.registration

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import com.rolling.meadows.utils.extensions.isValidEmail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterModel(
    @SerializedName("profile_file")
    var profileFile: String? = null,
    @SerializedName("first_name")
    var firstName: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("iso_code")
    var isoCode: String? = null,
    @SerializedName("phone_code")
    var phoneCode: String? = null,
    @SerializedName("phone_number")
    var phoneNumber: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("fcm_token")
    var fcmToken: String? = null,
    @SerializedName("device_type")
    var deviceType: Int? = null,
    @SerializedName("role")
    var role: String? = null,
    @SerializedName("confirm_password")
    var confirmPassword: String? = null,


    ) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            /* profileFile.isNullOrEmpty() -> {
                 errorProfilePicture = R.string.plz_select_profile_picture
             }*/
            firstName.isNullOrEmpty() -> {
                errorName = R.string.plz_enter_full_name
            }
            lastName.isNullOrEmpty() -> {
                errorLastName = R.string.plz_enter_last_name
            }

            (email?.trim() ?: "").isNotEmpty() && !email!!.trim().isValidEmail() -> {
                errorEmail = R.string.plz_enter_valid_email_address
            }

            phoneCode.isNullOrEmpty() -> {
                errorPhoneCode = R.string.plz_select_phone_code
            }
            phoneNumber.isNullOrEmpty() -> {
                errorPhoneNum = R.string.plz_enter_phone_number
            }
            phoneNumber!!.isNotEmpty() && phoneNumber!!.length < 6 -> {
                errorPhoneNum = R.string.plz_enter_valid_phone_num
            }
            password.isNullOrEmpty() -> {
                errorPassword = R.string.plz_enter_password
            }
            password!!.length < 8 -> {
                errorPassword = R.string.validPass
            }
            confirmPassword.isNullOrEmpty() -> {
                errorConfirmPassword = R.string.plz_enter_confirm_password
            }
            !confirmPassword!!.isNullOrEmpty() && (password.toString() != confirmPassword.toString()) -> {
                errorConfirmPassword = R.string.password_doesnot_matched_with_old_password
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun reset() {
        errorProfilePicture = null
        errorName = null
        errorLastName = null
        errorPassword = null
        errorPhoneNum = null
        errorPhoneCode = null
        errorPassword = null
    }
}