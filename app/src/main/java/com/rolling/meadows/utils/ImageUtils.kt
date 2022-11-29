package com.rolling.meadows.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.rolling.meadows.utils.extensions.showSingleChoiceAlertDialog
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ImageUtils() {
    var REQUEST_IMAGE_CAPTURE = 1
    private var imageCallBack: ((String?) -> Unit)? = null
    private var mActivity: Activity? = null

    init {

    }

    fun openImagePicker(activity: Activity?, requestCode: Int, callBack: ((String?) -> Unit)) {
        REQUEST_IMAGE_CAPTURE = requestCode
        imageCallBack = callBack
        mActivity = activity
        val imageArray = arrayOf("Camera", "Gallery")
        activity?.showSingleChoiceAlertDialog(
            imageArray,
            -1,
            title = "Upload Image",
            handleClick = {
                val intent: Intent
                when (it) {
                    0 -> {
                        intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                    }
                    else -> {
                        intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                    }

                }
                activity.startActivityForResult(
                    Intent.createChooser(intent, "Select picture"), REQUEST_IMAGE_CAPTURE
                )

            })

    }


    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_IMAGE_CAPTURE -> {
                        if (data != null) {
                            val selectedImageUri = data.data

                            if (selectedImageUri != null)  // image selected from gallary
                            {
                                val imagePath = getRealPathFromURI(mActivity!!, selectedImageUri)
                                imageCallBack?.invoke(imagePath)
                            } else {
                                // image captured from camera
                                val bitmap = data.extras?.get("data") as Bitmap?

                                val imagePath =
                                    getRealPathFromURI(
                                        mActivity!!,
                                        getImageUri(mActivity!!, bitmap)
                                    )
                                imageCallBack?.invoke(imagePath)

                            }
                        } else {
                            Log.d("==>", "Operation canceled!")
                        }
                    }
                }
            }
        }

    }


    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        if (inImage == null) {
            return null
        }

        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "IMG_" + timeStamp,
            null
        )
        return Uri.parse(path)
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        if (contentUri == null) {
            return null
        }

        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, projection, null, null, null)

            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            return column_index?.let {
                cursor?.getString(it)
            } ?: kotlin.run {
                null
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }
}