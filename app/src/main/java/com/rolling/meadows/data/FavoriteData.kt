package com.rolling.meadows.data
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteData(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("iso_code")
    var isoCode: String = "",
    @SerializedName("last_name")
    var lastName: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("phone_code")
    var phoneCode: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("profile_image")
    var profileImage: String = "",
    @SerializedName("rating")
    var rating: String = ""
):Parcelable