package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateData (
    @SerializedName("date")
    var date: String = "",
    @SerializedName("day")
    var day: String = ""
): Parcelable

