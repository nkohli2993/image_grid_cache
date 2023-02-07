package com.rolling.meadows.repository

import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.response_model.CategoriesReposnseModel
import com.rolling.meadows.data.response_model.EventDetailResponseModel
import com.rolling.meadows.data.response_model.EventResponseModel
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
class EventRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun eventList(
        filter_by: Int,
        date: String,
        endDate: String,
        page_limit: Int,
        page: Int,
        category_id: Int
    ): Flow<DataResult<EventResponseModel>> {
        return object : NetworkOnlineDataRepo<EventResponseModel, EventResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<EventResponseModel> {
                return apiService.eventList(filter_by, date,endDate, page_limit, page,category_id)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
    suspend fun eventDetail(
        eventId: Int
    ): Flow<DataResult<EventDetailResponseModel>> {
        return object : NetworkOnlineDataRepo<EventDetailResponseModel, EventDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<EventDetailResponseModel> {
                return apiService.eventDetail(eventId)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }



    suspend fun _logout(): Flow<DataResult<BaseResponseModel>> {
        return object : NetworkOnlineDataRepo<BaseResponseModel, BaseResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<BaseResponseModel> {
                return apiService.logout()
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }


    suspend fun getCategoriesList(): Flow<DataResult<CategoriesReposnseModel>> {
        return object : NetworkOnlineDataRepo<CategoriesReposnseModel, CategoriesReposnseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<CategoriesReposnseModel> {
                return apiService.getCategoriesList()
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }


}