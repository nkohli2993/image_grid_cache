package com.rolling.meadows.data.booking.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class RecentSearchHistoryResponseModel(
    val data: RecentData? = null
)

@Parcelize
data class RecentData(
    @SerializedName("list")
    val list: ArrayList<RecentListData> = ArrayList(),
    @SerializedName("current_page")
    var current_page: Int? = null,
    @SerializedName("per_page")
    var per_page: Int? = null,
    @SerializedName("total_pages")
    var total_pages: Int? = null
):Parcelable

@Parcelize
data class RecentListData(
    @SerializedName("user_id")
    val user_id: Int? = null,
    @SerializedName("pickup_area")
    val pickupArea: String? = null,
    @SerializedName("pickup_location", alternate = ["drop_location"])
    val pickupLocation: String? = null,
    @SerializedName("pickup_latitude", alternate = ["drop_latitude"])
    val pickupLatitude: String? = null,
    @SerializedName("pickup_longitude", alternate = ["drop_longitude"])
    val pickupLongitude: String? = null,
) : Parcelable

