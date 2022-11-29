package com.rolling.meadows.data.static_pages
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StaticPageModel(
    @SerializedName("description")
    var description: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("page_title")
    var pageTitle: String = "",
    @SerializedName("page_type")
    var pageType: Int = 0,
    @SerializedName("title")
    var title: String = ""
):Parcelable