package com.rolling.meadows.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    private var requestPermCode = 0
    private var permissionCallback: ((Boolean) -> Unit)? = null

    fun checkPermissions(
        context: Activity,
        perms: Array<String>,
        requestCode: Int,
        callback: ((Boolean) -> Unit)
    ) {
        permissionCallback = callback
        requestPermCode = requestCode
        val permissionsList = perms.filter {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_DENIED
        }
        if (permissionsList.isEmpty()) {
            permissionCallback?.invoke(true)
        } else {
            ActivityCompat.requestPermissions(
                context,
                permissionsList.toTypedArray(),
                requestPermCode
            )
        }

    }


    fun onRequestPermissionsResult(
        requestCode: Int,
        permssion: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestPermCode) {
            val notGrantedList = grantResults.filter { it == PackageManager.PERMISSION_DENIED }
            permissionCallback?.invoke(notGrantedList.isEmpty())
        }
    }
}



