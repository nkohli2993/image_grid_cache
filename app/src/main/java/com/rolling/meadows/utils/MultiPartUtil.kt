package com.rolling.meadows.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.webkit.MimeTypeMap
import com.rolling.meadows.di.provideAppContext
import com.rolling.meadows.utils.extensions.showException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException


fun String?.createMultipartBody(fieldName: String): MultipartBody.Part? {
    if (isNullOrEmpty()) {
        return null
    }
    var file: File? = null
    try {
        file = File(this)
        val requestBody = file?.let { createRequestBody(it) }
        if (requestBody != null)
            return MultipartBody.Part.createFormData(fieldName, file?.name, requestBody)

    } catch (e: FileNotFoundException) {
        showException(e)
    }
    return null
}

private fun createRequestBody(file: File): RequestBody? {
    val mime = getMimeType(Uri.fromFile(file))
    if (mime != null) {
        return file.asRequestBody(mime.toMediaTypeOrNull())
    }
    return null
}


fun String.getRequestBody(): RequestBody {
    return toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

fun ArrayList<String>.getRequestBody(): RequestBody {
    return this.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

fun Int.getRequestBody(): RequestBody {
    return this.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
}


fun getMimeType(uri: Uri): String? {
    var mimeType: String? = null
    if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
        val cr = provideAppContext()?.contentResolver
        mimeType = cr?.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
            uri
                .toString()
        )
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.toLowerCase()
        )
    }
    return mimeType
}


fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
    val stream = ByteArrayOutputStream()
    bitmap.compress(CompressFormat.JPEG, 70, stream)
    val byteFormat = stream.toByteArray()
    // get the base 64 string
    return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
}