package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonthCalendarData(
    @SerializedName("id")
    var id: Int = -1,
    @SerializedName("month_id")
    var monthId: Int = -1,
    @SerializedName("month")
    var month: String = "",
    @SerializedName("year")
    var year: String = "",
    @SerializedName("is_selected")
    var isSelected: Boolean = false
) : Parcelable

