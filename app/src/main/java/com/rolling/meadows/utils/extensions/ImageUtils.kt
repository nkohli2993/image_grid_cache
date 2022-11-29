package com.rolling.meadows.utils.extensions

import android.net.Uri
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

fun ImageView.loadCircleImage(url: String?, defaultImage: Int) {
    if (url.isNullOrEmpty()) {
        setImageResource(defaultImage)
    } else {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(this).load(url).placeholder(circularProgressDrawable).error(defaultImage)
            .into(this)
    }
}

fun ImageView.loadImage(url: String?, defaultImage: Int) {
    if (url.isNullOrEmpty()) {
        setImageResource(defaultImage)
    } else {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(this).load(url).placeholder(circularProgressDrawable).error(defaultImage)
            .into(this)
       /* load(url) {
            error(defaultImage)
        }*/
    }
}

val String.asUri: Uri?
    get() = try {
        if (URLUtil.isValidUrl(this))
            Uri.parse(this)
        else
            null
    } catch (e: Exception) {
        null
    }