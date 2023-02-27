package com.rolling.meadows.data.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListEventData (
    @SerializedName("child_list")
    var childList:  ArrayList<EventDetailData> ?= null,
    @SerializedName("name")
    var name: String = ""
): Parcelable