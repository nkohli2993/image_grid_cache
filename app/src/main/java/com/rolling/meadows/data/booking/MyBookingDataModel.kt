package com.rolling.meadows.data.booking

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyBookingDataModel (
    @SerializedName("list")
    val list: ArrayList<RideDetailData> = ArrayList(),
    @SerializedName("current_page")
    var current_page: Int? = null,
    @SerializedName("per_page")
    var per_page: Int? = null,
    @SerializedName("total_pages")
    var total_pages: Int? = null
) : Parcelable