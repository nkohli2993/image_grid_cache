package com.rolling.meadows.repository

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


}