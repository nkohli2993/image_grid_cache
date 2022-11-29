package com.rolling.meadows.data.booking

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentMethodData(

    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("card_id")
    val card_id: String,
    @SerializedName("card_number")
    val card_number: String,
    @SerializedName("full_name")
    val full_name: String,
    @SerializedName("exp_month")
    val exp_month: Int,
    @SerializedName("exp_year")
    val exp_year: Int,
    @SerializedName("default_card")
    val default_card: Int,
    @SerializedName("card_type")
    val card_type: String,
    @SerializedName("clover_customer_id")
    val clover_customer_id: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("updated_at")
    val updated_at: String,
    var isSelected: Boolean = false
) : Parcelable
