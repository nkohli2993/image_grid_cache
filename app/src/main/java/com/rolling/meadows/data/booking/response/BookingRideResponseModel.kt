package com.rolling.meadows.data.booking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.booking.RideDetailData
import com.rolling.meadows.data.response_model.BaseResponseModel
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BookingRideResponseModel (
    @SerializedName("data")
    var data: RideDetailData? = null
): BaseResponseModel(), Parcelable


