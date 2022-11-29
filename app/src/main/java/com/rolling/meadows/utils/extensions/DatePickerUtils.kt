package com.rolling.meadows.utils.extensions

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.view.View
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog
import com.rolling.meadows.R
import java.util.*


@SuppressLint("RestrictedApi")
fun View.openDatePickerDialog(
    setMinDate: Boolean = false,
    setMaxDate: Boolean = false,
    setMaxYear: Int = 0,
    onDateSelect: (calendar: Calendar) -> Unit,
    showTime: Boolean = false
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = MaterialStyledDatePickerDialog(
        this.context,R.style.dateTimeTheme,
        { view, year, monthOfYear, dayOfMonth ->

            val selectedCal = Calendar.getInstance()
            selectedCal.set(Calendar.YEAR, year)
            selectedCal.set(Calendar.MONTH, monthOfYear)
            selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (showTime) {
                openTimePickerDialog(selectedCal) { onDateSelect(it) }
            } else {
                onDateSelect(selectedCal)
            }

        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    if (setMinDate) {
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    if (setMaxDate) {
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        if (setMaxYear != 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -setMaxYear)
            datePickerDialog.datePicker.maxDate = cal.timeInMillis - 1000

        }
    }


    datePickerDialog.show()

}

fun View.openTimePickerDialog(
    calendar: Calendar = Calendar.getInstance(),
    is24Hour: Boolean = false,
    onTimeSelect: (calendar: Calendar) -> Unit
) {
    val timePickerDialog = TimePickerDialog(
        this.context,R.style.dateTimeTheme,
        { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            onTimeSelect(calendar)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24Hour
    )

    timePickerDialog.show()

}


fun getCurrentTime():Date?{
    val currentTime = Calendar.getInstance()
    currentTime.add(Calendar.SECOND,10)
    return currentTime.time
}
