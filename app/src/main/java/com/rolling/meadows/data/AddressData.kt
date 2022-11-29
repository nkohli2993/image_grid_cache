package com.rolling.meadows.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressData(

    @SerializedName("address")
    var address: String? = "",
    @SerializedName("id")
    var id: Int? = 0,

    @SerializedName("longitude")
    var longitude: String? = "",
    @SerializedName("latitude")
    var latitude: String? = "",
    @SerializedName("city")
    var city: String? = "",
    @SerializedName("state")
    var state: String? = "",
    @SerializedName("zipcode")
    var zipcode: String? = "",
    @SerializedName("country")
    var country: String? = "",

    @SerializedName("countryCode")
    var countryCode: String? = "",
    @SerializedName("first_name", alternate = ["name"])
    var firstName: String? = "",
    @SerializedName("last_name")
    var lastName: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("contact_no", alternate = ["number"])
    var contactNo: String? = "",
    var area :String? = "",
    var apartmentAddress: String? = "",
    var isZoomMap: Boolean = false,
    var plocation: String? = null,
    var plat: String? = null,
    var plng: String? = null,
    var dlocation: String? = null,
    var dlat: String? = null,
    var dlng: String? = null


) : Parcelable