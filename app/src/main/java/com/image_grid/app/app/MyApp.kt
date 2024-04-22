package com.image_grid.app.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
open class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this


    }

    companion object {
        var appContext: Application? = null
    }
}
