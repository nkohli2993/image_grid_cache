package com.rolling.meadows.utils

object ApiConstants {
    const val BASE_URL = "http://roves.itechnolabs.tech/api/v1/"
    const val SUCCESS = "success"

    object Authentication {
        const val LOGIN = "user/login"
        const val REGISTER = "user/register"
        const val FORGOT_PASSWORD = "user/forgotPasswordForPhoneNumber"

        const val VERIFY_EMAIL = "verify-email"
        const val VERIFY_OTP = "user/verifyPhoneNumberOtp"
        const val RESET_VERIFY_OTP = "user/verifyResetPasswordOtp"
        const val RESEND_CODE = "user/resendVerifyPhoneOtp"
        const val GET_PROFILE = "user/profile"
        const val LOGOUT = "user/logout"
        const val RESET_PASSWORD = "user/resetPassword"
        const val UPDATE_PROFILE = "user/updateProfile"
    }

    object Pages {
        const val CONTACT_US = "user/contactUs"
        const val STATIC_PAGES = "user/getPage"
        const val CHANGE_PASSWORD = "user/changePassword"
    }


    object ride {
        const val SEARCH_RIDE = "user/searchRide"
        const val RECENT_SEARCH_LIST = "user/recentHistory"
        const val CHECK_MY_RIDE_REQUEST = "user/check-my-current-ride"
        const val RETRY_RIDE_REQUEST = "user/retry-booking"
        const val BOOK_SCHEDULE_RIDE = "user/schedule-ride"
        const val CURRENT_RIDE = "user/my-current-ride"
        const val RATE_YOUR_RIDE = "user/rate-your-ride"
        const val PAST_RIDE = "user/my-rides"
        const val GET_RIDE_AMOUNT = "user/calculate-ride-amount"


        const val SEARCH_RIDE_VEHICLE = "user/chooseRideVehicle"
        const val BOOK_RIDE = "user/bookNow"
        const val MY_RIDE = "user/myRides"
        const val RIDE_DETAIL = "user/rideDetails"
        const val PAYMENT_SELECTION_METHOD_UPDATE = "user/payNow"
        const val API_CANCEL_RIDE = "user/cancelRide"
        const val API_ADD_RIDER_RATING = "user/rateYourRide"
    }


    object payment {
        const val ADD_CARD = "user/add-card"
        const val CARD_LISTING = "user/card-listing"
        const val CHECKOUT = "user/book-ride"
        const val PAY_TIP = "user/submit-and-pay-tip"
        const val REMOVE_CARD = "user/delete-card"
    }
    object Notification{
        const val API_NOTIFICATION_LIST = "user/notificationList"
    }
    object Favorite{
        const val API_FAVORITE_LIST = "user/favoriteDriverList"
        const val API_ADD_FAVORITE_LIST = "user/addToFavorite"
        const val API_REMOVE_FAVORITE_LIST = "user/removeToFavorite"
    }
}