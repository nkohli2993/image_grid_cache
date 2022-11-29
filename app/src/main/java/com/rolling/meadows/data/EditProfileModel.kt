package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.utils.extensions.isValidEmail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditProfileModel(
    @SerializedName("profile_file")
    var profileFile: String? = null,
    @SerializedName("first_name")
    var firstName: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null,
    @SerializedName("email")
    var email: String? = null
) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            firstName.isNullOrEmpty() -> {
                errorName = R.string.plz_enter_full_name
            }
            lastName.isNullOrEmpty() -> {
                errorLastName = R.string.plz_enter_last_name
            }
           /* email.isNullOrEmpty() -> {
                errorEmail = R.string.plz_enter_email_address
            }
            !email!!.isValidEmail() -> {
                errorEmail = R.string.plz_enter_valid_email_address
            }
*/
            (email?:"").isNotEmpty() && !email!!.isValidEmail() -> {
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
        errorName = null
        errorLastName = null
    }
}