package com.rolling.meadows.utils

object ApiConstants {
    const val BASE_URL = "https://rollingmeadows.itechnolabs.tech/api/v1/"
//    const val BASE_URL = "http://192.168.1.130:8000/api/v1/"
    const val SUCCESS = "success"

    object Authentication {
        const val LOGIN = "user/login"
        const val FORGOT_PASSWORD = "user/forgotPassword"
        const val VERIFY_OTP = "user/verifyOtp"
        const val RESEND_VERIFY_OTP = "user/resendOtp"
        const val RESET_PASSWORD = "user/resetPassword"
        const val GET_PROFILE = "user/profile"
        const val LOGOUT = "user/logout"
    }

    object Notification{
        const val API_NOTIFICATION_LIST = "user/notificationList"
        const val API_NOTIFICATION_READ = "user/notificatioRead"
        const val API_DELETE_NOTIFICATION = "user/deleteNotification"
    }
    object Event{
        const val API_EVENT_LIST = "user/myEvents"
        const val API_EVENT_DETAIL = "user/eventDetails"
        const val API_GET_CATEGORIES = "user/getEventCategories"
    }

}