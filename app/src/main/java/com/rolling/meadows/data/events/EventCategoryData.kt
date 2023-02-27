package com.rolling.meadows.data.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventCategoryData(
    @SerializedName("list_data")
    var listData:  ArrayList<ListEventData> ?= null,
//    @SerializedName("category")
//    var date: String = ""
) : Parcelable
