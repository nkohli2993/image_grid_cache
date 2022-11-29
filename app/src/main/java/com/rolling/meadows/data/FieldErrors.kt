package com.rolling.meadows.data

import android.os.Parcelable
import androidx.databinding.BaseObservable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FieldErrors : BaseObservable(), Parcelable {
    var errorPhoneCode: Int? = null
    var errorPhoneNum: Int? = null
    var errorName: Int? = null
    var errorLastName: Int? = null
    var errorProfilePicture: Int? = null
    var errorEmail: Int? = null
    var errorOtp: Int? = null
    var errorAddress: Int? = null
    var emptyField: Int? = null
    var errorPassword: Int? = null
    var errorOldPassword: Int? = null
    var errorConfirmPassword: Int? = null
    var errormessage: Int? = null
    var errorDescription: Int? = null
    var errorCategory: Int? = null
    var errorPickUpLocation: Int? = null
    var errorDropOffLocation: Int? = null
}