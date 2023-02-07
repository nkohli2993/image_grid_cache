package com.rolling.meadows.data
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryData(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = ""
):Parcelable