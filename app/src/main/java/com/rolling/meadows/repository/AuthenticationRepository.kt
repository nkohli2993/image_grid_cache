package com.rolling.meadows.repository

import com.rolling.meadows.data.authentication.*
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.response_model.LoginUserDetailModel
import com.rolling.meadows.data.response_model.UserDetailResponseModel
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
class AuthenticationRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun loginUser(value: LoginModel): Flow<DataResult<LoginUserDetailModel>> {
        return object : NetworkOnlineDataRepo<LoginUserDetailModel, LoginUserDetailModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<LoginUserDetailModel> {
                return apiService.login(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
    suspend fun loginWithOutEmail(value: LoginRequestModelData): Flow<DataResult<LoginUserDetailModel>> {
        return object : NetworkOnlineDataRepo<LoginUserDetailModel, LoginUserDetailModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<LoginUserDetailModel> {
                return apiService.loginWithOutEmail(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun verifyOTP(value: OTPVerificationModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.verifyOTP(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun resendVerificationCOde(value: OTPVerificationModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.resendVerificationCOde(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun forgotPassword(value: ForgotPasswordModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.forgotPassword(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun resetPassword(value: ResetPasswordModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.resetPassword(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun getProfile(): Flow<DataResult<UserDetailResponseModel>> {
        return object : NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.getProfile()
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

}