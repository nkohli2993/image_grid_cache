package com.rolling.meadows.data.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventListData(
    @SerializedName("current_page")
    var currentPage: Int = 0,
    @SerializedName("list")
    var list: ArrayList<EventData>? = null,
    @SerializedName("next_page")
    var nextPage: Boolean = false,
    @SerializedName("per_page")
    var perPage: String = "",
    @SerializedName("total")
    var total: Int = 0,
    @SerializedName("total_pages")
    var totalPages: Int = 0,
    @SerializedName("unread_notification_count")
    var unreadNotificationCount: Int = 0
):Parcelable


