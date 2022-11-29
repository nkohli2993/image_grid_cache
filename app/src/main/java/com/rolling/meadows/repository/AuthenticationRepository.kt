package com.rolling.meadows.repository

import com.rolling.meadows.data.EditProfileModel
import com.rolling.meadows.data.authentication.*
import com.rolling.meadows.data.authentication.registration.RegisterModel
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.response_model.LoginUserDetailModel
import com.rolling.meadows.data.response_model.UserDetailResponseModel
import com.rolling.meadows.data.static_pages.ChangePasswordModel
import com.rolling.meadows.network.retrofit.ApiService
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.NetworkOnlineDataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import com.rolling.meadows.utils.createMultipartBody
import com.rolling.meadows.utils.getRequestBody
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

    suspend fun registerUser(value: RegisterModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.registerUser(
                    value.profileFile.createMultipartBody("profile_file"),
                    value.email.toString().trim().getRequestBody(),
                    value.firstName.toString().trim().getRequestBody(),
                    value.lastName.toString().trim().getRequestBody(),
                    value.isoCode.toString().trim().getRequestBody(),
                    value.phoneCode.toString().trim().getRequestBody(),
                    value.phoneNumber.toString().trim().getRequestBody(),
                    value.password?.getRequestBody(),
                    value.fcmToken?.getRequestBody(),
                    value.deviceType?.getRequestBody(),
                    value.role?.getRequestBody()
                )
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
    suspend fun registerUserWithoutEmail(value: RegisterModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.registerUserWithoutEmail(
                    value.profileFile.createMultipartBody("profile_file"),
                    value.firstName.toString().trim().getRequestBody(),
                    value.lastName.toString().trim().getRequestBody(),
                    value.isoCode.toString().trim().getRequestBody(),
                    value.phoneCode.toString().trim().getRequestBody(),
                    value.phoneNumber.toString().trim().getRequestBody(),
                    value.password?.getRequestBody(),
                    value.fcmToken?.getRequestBody(),
                    value.deviceType?.getRequestBody(),
                    value.role?.getRequestBody()
                )
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun verifyPhoneNumber(value: OTPVerificationModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.verifyPhoneNumber(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun resetVerifyPhoneNumber(value: OTPVerificationModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.resetVerifyPhoneNumber(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun resendPhoneNUmberOtp(value: ResendOTPModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.resendCode(value)
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

    suspend fun changePassword(value: ChangePasswordModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.changePassword(value)
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

    suspend fun getProfile(): Flow<DataResult<UserDetailResponseModel>> {
        return object : NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.getProfile()
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

    suspend fun updateUserProfile(value: EditProfileModel): Flow<DataResult<UserDetailResponseModel>> {
        return object :
            NetworkOnlineDataRepo<UserDetailResponseModel, UserDetailResponseModel>() {
            override suspend fun fetchDataFromRemoteSource(): Response<UserDetailResponseModel> {
                return apiService.updateProfile(
                    value.profileFile.createMultipartBody("profile_file"),
                    value.firstName.toString().trim().getRequestBody(),
                    value.lastName.toString().trim().getRequestBody(),
                    value.email.toString().trim().getRequestBody()
                )
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }

}