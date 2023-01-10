package com.rolling.meadows.data.response_model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.data.events.EventData
import com.rolling.meadows.data.events.EventDetailData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventDetailResponseModel (
    @SerializedName("data") var data: EventDetailData? = null,
):Parcelable