package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationData(
    @SerializedName("created_at")
    var createdAt: String = "",
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
    @SerializedName("ride_id")
    var rideId: Int = 0,
    @SerializedName("sender_id")
    var senderId: Int = 0,
    @SerializedName("sender_image")
    var senderImage: String = "",
    @SerializedName("sender_name")
    var senderName: String = "",
    @SerializedName("time_ago")
    var timeAgo: String = ""
):Parcelable