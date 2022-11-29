package com.rolling.meadows.repository

import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.static_pages.ContactUsModel
import com.rolling.meadows.data.static_pages.StaticPageResponseModel
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
class StaticPageRepository@Inject constructor(private val apiService: ApiService) {

    suspend fun contactUs(value: ContactUsModel): Flow<DataResult<BaseResponseModel>> {
        return object : NetworkOnlineDataRepo<BaseResponseModel, BaseResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<BaseResponseModel> {
                return apiService.contactUs(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
    suspend fun getStaticPageInfo(value: String): Flow<DataResult<StaticPageResponseModel>> {
        return object : NetworkOnlineDataRepo<StaticPageResponseModel, StaticPageResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<StaticPageResponseModel> {
                return apiService.getStaticPageInfo(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }


}