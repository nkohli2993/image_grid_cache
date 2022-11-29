package com.rolling.meadows.data.booking

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RatingData(
    @SerializedName("created_by")
    var createdBy: Int = 0,
    @SerializedName("created_by_image")
    var createdByImage: String = "",
    @SerializedName("created_by_name")
    var createdByName: String = "",
    @SerializedName("driver_id")
    var driverId: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("rating")
    var rating: Int = 0,
    @SerializedName("review")
    var review: String = "",
    @SerializedName("user_id")
    var userId: Int = 0
):Parcelable