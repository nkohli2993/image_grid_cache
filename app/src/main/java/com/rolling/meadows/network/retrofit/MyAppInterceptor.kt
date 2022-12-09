package com.rolling.meadows.network.retrofit

import com.rolling.meadows.BuildConfig
import com.rolling.meadows.cache.CacheConstants
import com.rolling.meadows.cache.Prefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created Nikita kohli 
 */

class MyAppInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val headers = request.headers.newBuilder()
            .add("Content-Type", "application/json")
            .add("Accept", "application/json")
            .add("Authorization", "Bearer ${getToken()}")
            .add("appversion", BuildConfig.VERSION_CODE.toString())


            .build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }


    private fun getToken(): String? {
        return Prefs.getString(CacheConstants.USER_TOKEN, "")
    }
}