package com.rolling.meadows.data.booking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.response_model.BaseResponseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RideAmountResponse(
    @SerializedName("data")
    var data: RideTotal? = RideTotal()

) : BaseResponseModel(), Parcelable

@Parcelize
data class RideTotal(
    @SerializedName("total_amount")
    var total_amount: Double? = null
) : Parcelable
