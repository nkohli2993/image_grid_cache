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
    var time: String = "",
    @SerializedName("event_category_name")
    var event_category_name: String = "",
    @SerializedName("event_category_id")
    var event_category_id:  Int = 0,
):Parcelable