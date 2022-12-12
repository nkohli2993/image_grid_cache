package com.rolling.meadows.data.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventDetailData(
    @SerializedName("date")
    var date: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("event_type")
    var eventType: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("time")
    var time: String = ""
):Parcelable