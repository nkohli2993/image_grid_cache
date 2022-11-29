package com.rolling.meadows.utils;

object Constants {

    const val DEVICE_TYPE = 1
    const val DISPLAY_MESSAGE_ACTION = "com.rovertaxicustomer.DISPLAY_MESSAGE"

    const val PRIVACY = "privacy_policy"
    const val TERMSANDCONTION = "terms_condition"
    const val ABOUT_US = "about_us"
    const val SIGNUP = "sign_up"
    const val HOME = "home"
    const val PAYMENT = "payment"
    const val CHANGE_PASSWORD = "change_password"
    const val RESET_PASSWORD = "reset_password"
    const val PROFILE = "profile"
    const val CUSTOMER_ROLE = "1"
    const val PAGE_TYPE_ABOUT_US = "1"
    const val PAGE_TYPE_PRIVACY_POLICY = "2"
    const val PAGE_TYPE_TERMS_AND_CONDITION = "3"
    const val COUNTRY_CODE = "country_code"
    const val RIDE_ID = "ride_id"
    const val INPROGRESS_RIDES = 1
    const val COMPLETED_RIDES = 2


    enum class BookingType(val value: Int) {
        INPROGRESS(1),
        COMPLETED(2)
    }
    enum class PaymentType(val value: Int) {
        COD(1),
        ONLINE(2)
    }
    enum class PaymentMethod(val value: Int) {
        COD(1),
        ONLINE(2)
    }

    enum class BookingStatus(val value:Int){
        RIDE_PENDING(0),
        RIDE_ACCEPT(1),
        RIDE_GO_TO_PICKUP_LOCATION(2),
        RIDE_ON_GOING(3),
        RIDE_COMPLETED(4),
        RIDE_CANCEL(5),
        RIDE_REJECT(6),
        RIDE_AUTO_CANCEL(7),
        RIDE_AS_REACHED(8)
    }

    enum class NotificationType(val value: Int){
        NOTIFICATION_TYPE_CUSTOMER_SIGNUP (1),
        NOTIFICATION_TYPE_DRIVER_SIGNUP(2),
        NOTIFICATION_TYPE_DRIVING_LICENSE_EXPIRE(3),
        NOTIFICATION_TYPE_APPROVE_DRIVING_LICENSE(4),
        NOTIFICATION_TYPE_REJECT_DRIVING_LICENSE(5),
        NOTIFICATION_TYPE_APPROVE_VEHICLE(6),
        NOTIFICATION_TYPE_REJECT_VEHICLE(7),
        NOTIFICATION_TYPE_APPROVE_VEHICLE_DOCUMENTS(8),
        NOTIFICATION_TYPE_REJECT_VEHICLE_DOCUMENTS(9),
        NOTIFICATION_TYPE_APPROVE_BANK_DETAILS(10),
        NOTIFICATION_TYPE_REJECT_BANK_DETAILS(11),
        NOTIFICATION_TYPE_APPROVE_ACCOUNT(12),
        NOTIFICATION_TYPE_REJECT_ACCOUNT(13),
        NOTIFICATION_TYPE_RIDE_BOOK(14),
        NOTIFICATION_TYPE_RIDE_ACCEPTED(15),
        NOTIFICATION_TYPE_RIDE_ONGOING(16),
        NOTIFICATION_TYPE_RIDE_CANCELED(17),
        NOTIFICATION_TYPE_RIDE_REJECTED(18),
        NOTIFICATION_TYPE_RIDE_COMPLETED(19),
        NOTIFICATION_TYPE_RIDE_PICKUP(20),
        NOTIFICATION_TYPE_RIDE_AUTO_CANCEL(21),
        NOTIFICATION_TYPE_RIDE_AS_REACHED(22)
    }

    object drawerItem {
        const val PROFILE = 0
        const val MY_RIDE = 1
        const val CHANGE_PASSWORD = 2
        const val ABOUT_US = 3
        const val CONTACT_US = 4
        const val PRIVACY_POLICY = 5
        const val TERMS = 6
        const val LOGOUT = 7
    }

    object PermissionCode {
        const val IMAGE_CODE = 1002
        const val LOCATION_CODE = 10009
        const val AUTOCOMPLETE_REQUEST_CODE = 1009
        const val ACTIVITY_RESULT_CODE = 1
        const val HOME_ACTIVITY_RESULT_CODE = 165
        const val PERM_MAP_LOCATION = 101
        const val PERMISSION_LOCATION_CODE = 102

    }


    object dynamicPage {
        const val ABOUTUS = 1
        const val TERMS_CONDITION = 2
        const val PRIVACY_POLICY = 3
    }

    object category {
        const val FEEDBACK = 1
        const val SUGGESTION = 2
    }

    object OTPVERIFICATION {
        const val NOT_VERIFIED = 0
        const val VERIFIED = 1
    }

    object locationType {
        const val PICKUP = true
        const val DESTINATION = false
    }

    object cardType {
        const val VISA = "VISA"
        const val AMERICAN_EXPRESS = "AMEX"
        const val DINERS_CLUB = "DINERS_CLUB"
        const val JCB = "JCB"
    }

    object rideState {
        const val RIDE_PENDING = 0
        const val RIDE_ACCEPT = 1
        const val START_RIDE = 2
        const val RIDE_COMPLETE = 3
        const val RIDE_CANCEL = 4
        const val RIDE_REJECT = 5
        const val RIDE_SCHEDULE = 6
        const val RIDE_PICKUP = 7
        const val RIDE_ARRIVED = 8
    }

    object rideType {
        const val NORMAL_RIDE = 0
        const val SCHEDULED_RIDE = 1
        const val BOTH = 3
    }

    object paymentStatus {
        const val PAYMENT_COMPLETE = 1
        const val PAYMENT_PENDING = 0
    }

    object PAGE {
        const val TERMS_CONDTION = "https://wheretopetdispatch.itechnolabs.tech/term-condition"
        const val PRIVACY_POLICY = "https://wheretopetdispatch.itechnolabs.tech/privacy-policy"
    }


}