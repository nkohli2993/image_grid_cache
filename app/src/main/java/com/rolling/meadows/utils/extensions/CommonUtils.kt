package com.rolling.meadows.utils.extensions

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.rolling.meadows.BuildConfig
import com.rolling.meadows.R
import java.util.*

fun View.visibleView(show: Boolean) {
    visibility = if (show)
        View.VISIBLE
    else
        View.GONE
}

fun Int?.isNullOrZero(): Boolean {
    return this == null || this == 0
}

fun showLog(tag: String? = "catch_exception", message: String?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message ?: "")
    }
}

fun showException(exception: Exception?) {
    if (BuildConfig.DEBUG) {
        exception?.printStackTrace()
    }
}

@SuppressLint("SetTextI18n")
fun TextView.reservedWithVersion(context: Context) {
    text = "v${BuildConfig.VERSION_NAME.plus(" ${context.getString(R.string.all_right_reserved)}")}"
}

fun AppCompatEditText.onTextWritten(view: View) {
    doOnTextChanged { text, start, before, count ->
        if (text.toString().isNotEmpty()) {
            setBackgroundResource(R.drawable.background_black_stroke)
            view.visibleView(true)
        } else {
            setBackgroundResource(R.drawable.edittext_stroke)
            view.visibleView(false)
        }
    }
}

fun AppCompatImageView.showHidePassword(editView: AppCompatEditText) {
    setOnClickListener {
        if (this.tag == R.drawable.ic_show) {
            //Hide Password
            editView.transformationMethod = PasswordTransformationMethod.getInstance()
            this.setImageResource(R.drawable.ic_hide)
            this.tag = R.drawable.ic_hide

        } else {
            //Show password
            editView.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            this.setImageResource(R.drawable.ic_show)
            this.tag = R.drawable.ic_show
        }
//        isPasswordShown = !isPasswordShown
        editView.setSelection(editView.length())

    }
}

fun TextView.setSpanString(
    message: String?,
    color: Int = R.color.colorPrimary,
    startPos: Int,
    isBold: Boolean = false,
    isUnderLine: Boolean = false,

    endPos: Int = message?.length ?: 0,
    onClick: () -> Unit = {}
) {

    val ss = SpannableString(message)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            val typeFace = ResourcesCompat.getFont(context, R.font.poppins_semibold)
            ds.typeface = typeFace
            ds.color = ContextCompat.getColor(context, color)
            if (isBold) {
                ds.typeface = typeFace
            }
            if (isUnderLine) {
                ds.isUnderlineText = isUnderLine
            }

        }

    }

    ss.setSpan(clickableSpan, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = ss
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(context, R.color.transparent)

}


//change status bar color
fun AppCompatActivity.setStatusBarColor(
    color: Int = R.color.colorPrimaryDark,
    showLight: Boolean = false
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = this.window

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)


    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = this.window.decorView
        view.let {
            if (showLight) {

                it.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            } else {
                it.systemUiVisibility =
                    it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //set status text  light

            }
        }
    }
}

/**
 * Toast extensions.
 */

fun Context.showSmallLengthToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.showLongLengthToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}


/**
 * Snackbar's extensions.
 */

fun View.showSmallSnackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
}

fun View.showLongSnackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}

fun View.showIndefiniteSnackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_INDEFINITE).show()
}


fun openURLonWeb(context: Context?, url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    context?.startActivity(intent)
}


fun openDialer(context: Context, contact: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$contact")
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

fun openEmail(context: Context, email: String) {
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.setType("plain/text");
    emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."))
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }

}

fun openLocation(context: Context, lat: String, lng: String, address: String) {
    val strUri =
        "http://maps.google.com/maps?q=loc:" + lat.toString() + "," + lng.toString() + " (" + address + ")"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }

}


fun changeCardNumber(cardno: String): String {
    val buf = StringBuffer(cardno)
    when (cardno.length) {
        14 -> {
            buf.replace(4, cardno.length - 4, " **** **")
        }
        15 -> {
            buf.replace(4, cardno.length - 4, " **** ***")
        }
        16 -> {
            buf.replace(4, cardno.length - 4, " **** **** ")
        }
    }

    return buf.toString()
}


fun getTimeZone(): String {
    val tz: TimeZone = TimeZone.getDefault()
    return tz.id
}
