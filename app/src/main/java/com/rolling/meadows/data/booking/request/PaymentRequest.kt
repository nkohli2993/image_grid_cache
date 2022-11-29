package com.rolling.meadows.data.booking.request

data class PaymentRequest(

    var full_name: String? = null,
    var card_number: String? = null,
    var exp_month: String? = null,
    var exp_year: String? = null,
    var ride_id: Int? = null,
    var card_id: Int? = null,
    var cvv: Int? = null,
    var schedule_date: String? = null,
    var schedule_time: String? = null,
    var pet_name: String? = null,
    var pickup_location: String? = null,
    var pickup_latitude: String? = null,
    var pickup_longitude: String? = null,
    var drop_location: String? = null,
    var drop_latitude: String? = null,
    var drop_longitude: String? = null,
    var amount: String? = null,
    var rating: String? = null,
    var review: String? = null,
    @Transient
    var rideType :Int?=null
)
