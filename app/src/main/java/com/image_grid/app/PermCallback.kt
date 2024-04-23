package com.image_grid.app

interface PermCallback {
    fun permGranted(resultCode: Int)

    fun permDenied(resultCode: Int)
}
