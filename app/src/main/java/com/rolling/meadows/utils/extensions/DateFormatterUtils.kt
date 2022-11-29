package com.rolling.meadows.utils.extensions

import com.rolling.meadows.BuildConfig
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String?.changeDateFormat(
    sourceDateFormat: String? = "yyyy-MM-dd",
    targetDateFormat: String? = "dd MMM, yyyy"
): String {
    if (isNullOrEmpty()) {
        return ""
    }
    return createDate(sourceDateFormat).getDate(targetDateFormat)
}


fun Date?.getDate(format: String? = "yyyy-MM-dd"): String {
    if (this == null || format.isNullOrEmpty()) {
        return ""
    }
    val outputDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return outputDateFormat.format(this)
}


fun String?.createDate(sourceFormat: String? = "yyyy-MM-dd"): Date {
    if (isNullOrEmpty()) {
        return Date()
    }
    val inputDateFromat = SimpleDateFormat(sourceFormat, Locale.getDefault())
    var date = Date()
    try {
        date = inputDateFromat.parse(this)
    } catch (e: ParseException) {
        logStack(e)
    }

    return date
}


fun Long?.createDate(): Date {
    val inputDateFromat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date = Date()

    this?.let {
        try {
            date = inputDateFromat.parse(Date(this).toString())
        } catch (e: ParseException) {
            logStack(e)
        }
    }
    return date
}

fun logStack(e: Exception) {
    if (BuildConfig.DEBUG) {
        e.printStackTrace()
    }
}

fun changeDateFormatFromDate(sourceDate: Date?, targetDateFormat: String?): String {
    if (sourceDate == null || targetDateFormat == null || targetDateFormat.isEmpty()) {
        return ""
    }
    val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
    return outputDateFormat.format(sourceDate)
}
fun currentYearLastTwoDigit():Int
{
    val df: DateFormat = SimpleDateFormat("yy") // Just the year, with 2 digits

    val formattedDate: String = df.format(Calendar.getInstance().getTime())
    return formattedDate.toInt()
}