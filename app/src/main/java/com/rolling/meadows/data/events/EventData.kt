package com.rolling.meadows.data.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventData(
    @SerializedName("data")
    var `data`: ArrayList<EventDetailData> = arrayListOf(),
    @SerializedName("date")
    var date: String = ""
) : Parcelable
