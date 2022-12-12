package com.rolling.meadows.utils

object ApiConstants {
    const val BASE_URL = "http://rollingmeadows.itechnolabs.tech/api/v1/"
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

    object Pages {
        const val CONTACT_US = "user/contactUs"
        const val STATIC_PAGES = "user/getPage"
        const val CHANGE_PASSWORD = "user/changePassword"
    }

    object Notification{
        const val API_NOTIFICATION_LIST = "user/notificationList"
    }
    object Event{
        const val API_EVENT_LIST = "user/myEvents"
        const val API_EVENT_DETAIL = "user/eventDetails"
    }

}