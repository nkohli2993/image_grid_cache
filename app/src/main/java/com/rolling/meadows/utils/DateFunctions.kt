package com.rolling.meadows.utils
import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateFunctions {

    var TAG = DateFunctions::class.java.simpleName
    var YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    val yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss"
    val EEE_DD_MMM_YYYY = "EEE, dd MMM, yyyy"
    val EEE_DD_MMM_YYYY_hh_mm = "EEE, dd MMM, yyyy HH:mm"
    val EEE_DD_MMM= "EEE, dd MMM"
    val dd_MMM_yyyy_comma = "dd MMM, yyyy"
    val dd_MMM_yyyy_hh_mm_a_comma = "dd MMM,yyyy hh:mm a"
    val dd_MMM_yyyy_hh_mm = "dd MMM,yyyy HH:mm"
    val dd_MMM = "dd MMM"
    val MMM_dd_yyyy = "MMM dd, yyyy"
    val EEE_MMM_dd_yyyy = "EEE, MMM dd yyyy"
    val MMM_dd_yyyy_HH_mm = "MMM dd, yyyy HH:mm"
    val dd_MMM_yyyy = "dd MMM yyyy"
    val yyyy_MM_dd = "yyyy-MM-dd"
    val hh_mm_a = "hh:mm a"
    val HH_mm = "HH:mm"
    val HH_mm_ss = "HH:mm:ss"
    val MMMM_DD_YYYY_hh_mm_ss_a = "hh:mm a"


    fun tweleveHoursFormat(date: String): String? {
        val inputPattern = "HH:mm"
        val outputPattern = "hh:mm a"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var newDate: Date? = null
        var str: String? = null

        try {
            newDate = inputFormat.parse(date)
            str = outputFormat.format(newDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }


    fun timeStampToDateString(timeStamp: Long, timeFormat: String): String {
        val sdf = SimpleDateFormat(timeFormat)
        val netDate = Date(timeStamp)
        val date = sdf.format(netDate)
        return date

    }

    fun timeStampToDateFromUTC(timeStamp: Long, timeFormat: String): String {
        val sdf = SimpleDateFormat(timeFormat)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val utcDate = Date(timeStamp)

        val dateFormatter = SimpleDateFormat(timeFormat) //this format changeable
        dateFormatter.timeZone = TimeZone.getDefault()
        val date = dateFormatter.format(utcDate)
        return date

    }
    fun convertDateToString(date: Date?, format: String?): String? {
        var dateStr: String? = null
        val df: DateFormat = SimpleDateFormat(format)
        try {
            dateStr = df.format(date)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return dateStr
    }

    fun timeStampToDate(timeStamp: Long, timeFormat: String): String {
        val sdf = SimpleDateFormat(timeFormat)
        val netDate = Date(timeStamp)
        val date = sdf.format(netDate)
        return date

    }


    fun convertDateFormatFromUTC(
        originalFormat: String,
        targetFormat: String,
        apiDate: String
    ): String {

        var ourDate: String? = apiDate
        try {
            val formatter = SimpleDateFormat(originalFormat, Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(ourDate)
            val dateFormatter =
                SimpleDateFormat(targetFormat, Locale.getDefault()) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourDate = dateFormatter.format(value)
            Log.d("ourDate", ourDate)
        } catch (e: Exception) {
            ourDate = "00-00-0000 00:00"
        }

        return ourDate!!
    }

    fun convertDateFormatToUTC(
        originalFormat: String,
        targetFormat: String,
        apiDate: String
    ): String {
        val df = SimpleDateFormat(originalFormat)
        df.timeZone = TimeZone.getDefault()
        val date: Date = df.parse(apiDate)


        val dateFormatter = SimpleDateFormat(originalFormat) //this format changeable
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val formattedDate: String = dateFormatter.format(date)

        return getFormattedDate(
            originalFormat,
            targetFormat,
            formattedDate
        )
    }


    fun getFormattedDate(
        originalFormatString: String,
        targetFormatString: String,
        inputDate: String
    ): String {
        val originalFormat = SimpleDateFormat(originalFormatString)
        val targetFormat = SimpleDateFormat(targetFormatString)
        return targetFormat.format(originalFormat.parse(inputDate))
    }


    fun age(birthday: Date?, date: Date?): Int {
        val formatter: DateFormat = SimpleDateFormat("yyyyMMdd")
        val d1: Int = formatter.format(birthday).toInt()
        val d2: Int = formatter.format(date).toInt()
        return (d1 - d2) / 10000
    }

    @Throws(ParseException::class)
    fun stringDateToDate(date: String?, format: String): Date? {
        val sdf = SimpleDateFormat(format)
        val strDate = date
        return sdf.parse(strDate)
    }


    fun getCurrentTime(): Date? {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat(HH_mm)
        val time = sdf.format(cal.time)
        return stringDateToDate(time, HH_mm)
    }
    fun getCurrentDate(format: String): Date? {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat(format)
        val time = sdf.format(cal.time)
        return stringDateToDate(time, format)
    }

    fun getCurrentDateTime(format: String): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat(format, Locale.getDefault())
        val formattedDate = df.format(c)
        return formattedDate
    }

    fun dateFromUTC(date: Date,format: String): String {
        val df = SimpleDateFormat(format)
        df.timeZone = TimeZone.getTimeZone("gmt")
        val gmtTime = df.format(date)
        return gmtTime
    }

    fun dateToUTC(date: Date): Date? {
        Log.d(
            TAG,
            "dateToUTC=" + Date(date.time - Calendar.getInstance().timeZone.getOffset(date.time)).toString()
        )
        return Date(date.time - Calendar.getInstance().timeZone.getOffset(date.time))
    }

    fun compareDateTime(inTime: Date, outTime: Date): Boolean {
        when (inTime.compareTo(outTime)) {
            0 -> {
                return false
            }
            1 -> {
                return false
            }
            -1 -> {
                return true
            }
        }
        return false
    }

}