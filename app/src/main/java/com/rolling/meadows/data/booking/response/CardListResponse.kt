package com.rolling.meadows.data.booking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.booking.PaymentMethodData
import com.rolling.meadows.data.response_model.BaseResponseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardListResponse (
    @SerializedName("data")
    var paymentList: ArrayList<PaymentMethodData> = ArrayList()
): BaseResponseModel(),Parcelable
