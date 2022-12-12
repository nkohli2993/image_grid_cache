package com.rolling.meadows.data.response_model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.events.EventListData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventResponseModel (
    @SerializedName("data") var data: EventListData? = null,
):Parcelable