package com.rolling.meadows.utils.bindingUtils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.rolling.meadows.utils.extensions.changeDateFormat
import com.rolling.meadows.utils.extensions.loadCircleImage
import com.rolling.meadows.utils.extensions.loadImage

@BindingAdapter(value = ["imageUrl", "defaultImage"], requireAll = false)
fun setImage(imageView: ImageView, url: String?, defaultImage: Int = 0) {
    imageView.loadImage(url, defaultImage)
}

@BindingAdapter(value = ["circleImageUrl", "defaultImage"], requireAll = false)
fun setCircleImage(imageView: ImageView, url: String?, defaultImage: Int = 0) {
    imageView.loadCircleImage(url, defaultImage)
}

@BindingAdapter(value = ["validationError", "enableTouch"], requireAll = false)
fun setError(textInputLayout: TextInputLayout, error: Int?, enableTouch: Boolean? = true) {
    if (error == null || error == 0) {
        textInputLayout.error = null
    } else {
        textInputLayout.error = textInputLayout.context.getString(error)
        textInputLayout.editText?.requestFocus()
        textInputLayout.editText?.clearFocus()
        if (enableTouch == null || enableTouch) {
            textInputLayout.editText?.setOnTouchListener { v, event ->
                textInputLayout.error = null
                return@setOnTouchListener false
            }
        }
    }
}

@BindingAdapter(value = ["validationError"], requireAll = false)
fun setTextViewError(textView: TextView, error: Int?) {
    if (error == null || error == 0) {
       // textView.visibility = View.GONE
    } else {
        textView.visibility = View.VISIBLE
        textView.requestFocus()
        textView.error = textView.context.getString(error)

    }
}


@BindingAdapter(value = ["setDate", "sourceFormat", "outputFormat"], requireAll = false)
fun setDate(textView: TextView, date: String?, sourceFormat: String?, outputFormat: String?) {
    textView.text =
        date.changeDateFormat(sourceFormat ?: "yyyy-MM-dd", outputFormat ?: "dd MMM,yyyy")
}

