package com.rolling.meadows.utils;

object Constants {

    const val DEVICE_TYPE = 1
    const val DISPLAY_MESSAGE_ACTION = "com.rollingMeadows.DISPLAY_MESSAGE"

    const val RESET_PASSWORD = "reset_password"


    enum class EVENT_FILTER_TYPE(val value:Int){
        DAY(1),
        WEEK(2),
        MONTH(3),

    }
}