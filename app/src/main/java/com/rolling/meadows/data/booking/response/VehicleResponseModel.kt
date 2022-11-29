package com.rolling.meadows.data.booking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.booking.VehicleDataModel
import com.rolling.meadows.data.response_model.BaseResponseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VehicleResponseModel(
    @SerializedName("list")
    val dataList: ArrayList<VehicleDataModel> = ArrayList(),
    @SerializedName("current_page")
    var current_page: Int? = null,
    @SerializedName("per_page")
    var per_page: Int? = null,
    @SerializedName("total_pages")
    var total_pages: Int? = null
) : BaseResponseModel(), Parcelable

