package com.rolling.meadows.utils.extensions

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.IdRes

fun TextView.content(): String = text.toString().trim()

fun TextView.isBlank(): Boolean = text.toString().trim().isNullOrEmpty()

fun EditText.editable(enable: Boolean) {
    isFocusable = enable
    isFocusableInTouchMode = enable
    isCursorVisible=enable
    }

fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Activity.findAndSetTextInTextView(@IdRes id: Int, text: String) {
    findViewById<TextView>(id).text = text
}

fun Activity.findAndSetTextInEditText(@IdRes id: Int, text: String) {
    findViewById<EditText>(id).setText(text)
}

fun EditText.otpHelper() {

    setOnKeyListener { v, keyCode, event ->

        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (isBlank()) {
                val view = focusSearch(View.FOCUS_LEFT)
                view?.requestFocus()

            }
        }

        false
    }
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().length == 1) {
                val view = focusSearch(View.FOCUS_RIGHT)
                view?.let { it.requestFocus() } ?: run {
                    this@otpHelper.hideKeyBoard()
                }
            }

        }
    })

}
fun View.hideKeyBoard() {

    this.let {
        val imm =
            this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            true
        }
        false
    }
}
