package com.rolling.meadows.di

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceAutoComplete(val placeId: String, val address: String, val area: String,var lat:String = "", var lng:String = ""):Parcelable