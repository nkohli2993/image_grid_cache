package com.rolling.meadows.data.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventData(
    @SerializedName("data")
//    var `data`: ArrayList<EventCategoryData> = arrayListOf(),
    var `data`: EventCategoryData? = null,
    @SerializedName("date")
    var date: String = ""
) : Parcelable
