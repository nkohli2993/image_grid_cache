package com.image_grid.app.network.retrofit
import com.image_grid.app.ApiConstants
import com.image_grid.app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/**
 * Created by Nikita kohli 
 */

import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor { //for logs
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    var appInterceptor: Interceptor? = null

    @Provides
    @Singleton
    fun provideHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (appInterceptor == null)
            appInterceptor = MyAppInterceptor()

        httpClient.addInterceptor(appInterceptor!!)
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(logging)
        }
        httpClient.connectTimeout(2, TimeUnit.MINUTES)
        httpClient.writeTimeout(2, TimeUnit.MINUTES)
        httpClient.readTimeout(2, TimeUnit.MINUTES)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideApiProvider(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}