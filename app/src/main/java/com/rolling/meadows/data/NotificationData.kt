package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class NotificationData(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("event_id")
    var eventId: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("notification_type")
    var notificationType: String = "",
    @SerializedName("read")
    var read: String = "",
    @SerializedName("receiver_id")
    var receiverId: Int = 0,
    @SerializedName("sender_id")
    var senderId: Int = 0,
    @SerializedName("sender_image")
    var senderImage: String = "",
    @SerializedName("sender_name")
    var senderName: String = "",
    @SerializedName("time_ago")
    var timeAgo: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("event_type")
    var eventType: String = "",
    @SerializedName("date")
    var date: String = "",
    @SerializedName("time")
    var time: String = "",
    @SerializedName("description")
    var description: String = "",
):Parcelable