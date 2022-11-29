package com.rolling.meadows.utils.extensions

import android.app.Service
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.rolling.meadows.R
import com.rolling.meadows.utils.EspressoIdlingResource
import com.rolling.meadows.utils.SingleEvent
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}


/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.decrement()
            }
        })
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> {
                    hideKeyboard()
                    showSnackbar(it, timeLength)
                }
                is Int -> {
                    hideKeyboard()
                    showSnackbar(this.context.getString(it), timeLength)
                }
                else -> {
                }
            }

        }
    })
}

fun View.showToast(
    lifecycleOwner: LifecycleOwner,
    ToastEvent: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {

    ToastEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Toast.makeText(this.context, it, timeLength).show()
                is Int -> Toast.makeText(this.context, this.context.getString(it), timeLength)
                    .show()
                else -> {
                }
            }
        }
    })
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun ImageView.loadImage(@DrawableRes resId: Int) = Picasso.get().load(resId).into(this)
fun ImageView.loadImage(url: String) =
    Picasso.get().load(url).placeholder(R.drawable.ic_image_placeholder)
        .error(R.drawable.ic_image_placeholder).into(this)

fun ImageView.loadImage(@DrawableRes placeHolder: Int, url: String) =
    Picasso.get().load(url).placeholder(placeHolder).error(placeHolder).into(this)


fun ImageView.loadImage(@DrawableRes placeHolder: Int, file: File) =
    Picasso.get().load(file).placeholder(placeHolder).error(placeHolder).into(this)


fun ImageView.loadImageCentreCrop(@DrawableRes placeHolder: Int, file: File) =
//    Picasso.get().load(file).placeholder(placeHolder).error(placeHolder).fit().centerInside().into(this)
    Glide.with(this).load(file).placeholder(placeHolder).error(placeHolder).centerCrop().into(this);

fun ImageView.loadImageCentreCrop(@DrawableRes placeHolder: Int, file: String) =
//    Picasso.get().load(file).placeholder(placeHolder).error(placeHolder).fit().centerInside().into(this)
    Glide.with(this).load(file).placeholder(placeHolder).error(placeHolder).into(this);

fun AppCompatTextView.setTextFutureExt(text: String) =
    setTextFuture(
        PrecomputedTextCompat.getTextFuture(
            text,
            TextViewCompat.getTextMetricsParams(this),
            null
        )
    )

fun AppCompatEditText.setTextFutureExt(text: String) =
    setText(
        PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))
    )


fun TextView.attributedString(
    forText: String,
    foregroundColor: Int? = null,
    style: StyleSpan? = null
) {
    val spannable: Spannable = SpannableString(text)

    // check if the text we're highlighting is empty to abort
    if (forText.isEmpty()) {
        return
    }

    // compute the start and end indices from the text
    val startIdx = text.indexOf(forText)
    val endIdx = startIdx + forText.length

    // if the indices are out of bounds, abort as well
    if (startIdx < 0 || endIdx > text.length) {
        return
    }

    // check if we can apply the foreground color
    foregroundColor?.let {
        spannable.setSpan(
            ForegroundColorSpan(it),
            startIdx,
            endIdx,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }

    // check if we have a stylespan
    style?.let {
        spannable.setSpan(
            style,
            startIdx,
            endIdx,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }

    // apply it
    text = spannable
}

fun TextInputEditText.datePicker(fm: FragmentManager, tag: String) {
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(context.getString(R.string.select_date))
            .build()


    setOnClickListener {
        datePicker.show(fm, tag)
    }

    datePicker.addOnPositiveButtonClickListener {
        setText(datePicker.headerText)
    }
}

fun AppCompatTextView.datePicker(fm: FragmentManager, tag: String) {
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText(context.getString(R.string.select_date))
            .build()


    setOnClickListener {
        if (!datePicker.isAdded) {
            datePicker.show(fm, tag)
        }
    }

    datePicker.addOnPositiveButtonClickListener {
        text = datePicker.headerText
    }
}

fun AppCompatTextView.timePicker(context: Context) { val mCurrentTime = Calendar.getInstance()
    val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mCurrentTime.get(Calendar.MINUTE)

    val mTimePicker = TimePickerDialog(
        context,
        { view, hourOfDay, selectedMinute ->
            text = String.format("%02d:%02d", hourOfDay, selectedMinute)
        }, hour,
        minute, true
    )

    setOnClickListener { v ->
        mTimePicker.show()
    }
}


