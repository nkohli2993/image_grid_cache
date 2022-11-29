package com.rolling.meadows.data.booking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.booking.MyBookingDataModel
import com.rolling.meadows.data.response_model.BaseResponseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyBookingResponseModel (
    @SerializedName("data")
    var data: MyBookingDataModel? = null
): BaseResponseModel(), Parcelable