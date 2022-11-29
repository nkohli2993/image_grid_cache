package com.rolling.meadows.utils.extensions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.rolling.meadows.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


//common method to show alert dialog
fun Context.showAlertDialog(
    message: String = "",
    title: String = this.getString(R.string.alert),
    postiveBtnText: String = this.getString(R.string.ok),
    negativeBtnText: String = this.getString(R.string.cancel),
    handleClick: (positiveClick: Boolean) -> Unit = {},
    isCancelable: Boolean = true
) {


    val builder = MaterialAlertDialogBuilder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(isCancelable)
    builder.setPositiveButton(postiveBtnText) { dialog, which ->
        dialog.dismiss()
        handleClick(true)
    }
    if (negativeBtnText.isNotEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()
            handleClick(false)

        }
    }
    val dialog = builder.create()
    dialog.show()

}


fun Context.showSingleChoiceAlertDialog(
    dataArray: Array<String>,
    selectedPosition: Int = -1,
    title: String = this.getString(R.string.alert),
    negativeBtnText: String = this.getString(R.string.cancel),
    handleClick: (selectedPosition: Int) -> Unit = {},
    isCancelable: Boolean = true
) {

    val builder = MaterialAlertDialogBuilder(this)
    builder.setTitle(title)
    builder.setSingleChoiceItems(
        dataArray, selectedPosition
    ) { dialog, which ->
        dialog?.dismiss()
        handleClick(which)
    }

    builder.setCancelable(isCancelable)
    if (!negativeBtnText.isEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()

        }
    }
    val dialog = builder.create()
    dialog.show()

}


fun Context.showMultiChoiceAlertDialog(
    dataArray: Array<String>,
    selectedArray: BooleanArray,
    title: String = this.getString(R.string.alert),
    positiveBtnText: String = this.getString(R.string.ok),
    negativeBtnText: String = this.getString(R.string.cancel),
    handleClick: (selectedArray: BooleanArray) -> Unit = {},
    isCancelable: Boolean = true
) {

    val builder = MaterialAlertDialogBuilder(this)
    builder.setTitle(title)
    builder.setMultiChoiceItems(
        dataArray, selectedArray
    ) { dialog, which, isChecked -> selectedArray[which] = isChecked }

    builder.setPositiveButton(positiveBtnText) { dialog, which ->
        handleClick(selectedArray)
        dialog.dismiss()
    }
    builder.setCancelable(isCancelable)
    if (!negativeBtnText.isEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()

        }
    }
    val dialog = builder.create()
    dialog.show()

}
fun Context.buildAlertMessageNoGps() {
    var gpsAlert: AlertDialog? = null
    if (gpsAlert != null && gpsAlert.isShowing) {
        gpsAlert.dismiss()
    }
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage(getString(R.string.your_gps_seems_to_be_disable))
        .setCancelable(false)
        .setPositiveButton(getString(R.string.yes)) { dialog, which ->
            dialog.dismiss()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }
    gpsAlert = builder.create()
    gpsAlert.show()
    gpsAlert.getButton(AlertDialog.BUTTON_NEGATIVE).background = ContextCompat.getDrawable(this, R.color.full_transparent)
    gpsAlert.getButton(AlertDialog.BUTTON_POSITIVE).background = ContextCompat.getDrawable(this, R.color.full_transparent)
    gpsAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
    gpsAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
}