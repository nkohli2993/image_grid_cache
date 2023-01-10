package com.rolling.meadows.repository

import com.rolling.meadows.data.NotificationReadModel
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.response_model.NotificationReadResponseModel
import com.rolling.meadows.data.response_model.NotificationResponseModel
import com.rolling.meadows.network.retrofit.ApiService
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.NetworkOnlineDataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getNotificationList(page:Int,limit:Int): Flow<DataResult<NotificationResponseModel>> {
        return object : NetworkOnlineDataRepo<NotificationResponseModel, NotificationResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<NotificationResponseModel> {
                return apiService.notificationList(limit,page)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
    suspend fun notificationRead(id:Int): Flow<DataResult<NotificationReadResponseModel>> {
        return object : NetworkOnlineDataRepo<NotificationReadResponseModel, NotificationReadResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<NotificationReadResponseModel> {
                return apiService.notificationRead(NotificationReadModel(id))
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
    suspend fun deleteNotification(id:Int): Flow<DataResult<BaseResponseModel>> {
        return object : NetworkOnlineDataRepo<BaseResponseModel, BaseResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<BaseResponseModel> {
                return apiService.deleteNotification(NotificationReadModel(id))
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }


}