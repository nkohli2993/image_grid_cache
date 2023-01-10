package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationReadModel(
    @SerializedName("notification_id")
    var notificationId: Int? = null
) : Parcelable