package com.rolling.meadows.app

import android.app.Application
import com.rolling.meadows.di.modelModule
import com.rolling.meadows.di.networkingModules
import com.rolling.meadows.di.repositoryModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@HiltAndroidApp
open class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        startKoin {
            androidLogger(Level.NONE)

            androidContext(this@MyApp)
            modules(
                listOf(
                    networkingModules,
                    modelModule,
                    repositoryModule
                )
            )
        }

    }

    companion object {
        var appContext: Application? = null
    }
}
