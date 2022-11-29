package com.rolling.meadows.data.static_pages

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import com.rolling.meadows.utils.extensions.isValidEmail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactUsModel(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("category")
    var category: String? = null,
    @SerializedName("description")
    var description: String? = null
) : FieldErrors(), Parcelable {

    fun isValid(): Boolean {
        reset()
        when {
            name.isNullOrEmpty()->{
                errorName = R.string.plz_enter_name
            }
            email.isNullOrEmpty() -> {
                errorEmail = R.string.plz_enter_email_address
            }
            !email!!.isValidEmail() -> {
                errorEmail = R.string.plz_enter_valid_email_address
            }
            category.isNullOrEmpty() -> {
                errorCategory = R.string.plz_select_category
            }
            description.isNullOrEmpty() -> {
                errorDescription = R.string.plz_enter_description
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun reset() {
        errorName = null
        errorAddress = null
        errorEmail = null
        errorDescription = null
        errorCategory = null
    }
}