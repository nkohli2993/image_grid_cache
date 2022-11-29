package com.rolling.meadows.di

import android.content.Context
import com.rolling.meadows.app.MyApp
import com.rolling.meadows.repository.AuthenticationRepository
import com.rolling.meadows.utils.ImageUtils
import com.rolling.meadows.socket.SocketManager
import org.koin.dsl.module


val networkingModules = module {
   /* single {
        provideLoggingInterceptor()
    }
    single {
        provideHttpClient(get())
    }
    single {
        provideApiProvider(get())
    }
    single {
        provideApiService(get())
    }*/
    single {
        provideSocketManager(get())
    }
}


fun provideAppContext(): Context {
    return MyApp.appContext!!
}

fun provideSocketManager(context: Context): SocketManager {
    return SocketManager()
}

val repositoryModule = module {
    single {
        AuthenticationRepository(get())
    }
    single {
        RideRepository(get(),get())
    }
    single {
        ImageUtils()
    }
}

val modelModule = module {
//    viewModel {
//        AuthenticationViewModel(get(), get())
//    }
}

