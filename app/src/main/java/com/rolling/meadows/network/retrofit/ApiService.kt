package com.rolling.meadows.network.retrofit

import com.rolling.meadows.data.authentication.*
import com.rolling.meadows.data.booking.response.*
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.response_model.LoginUserDetailModel
import com.rolling.meadows.data.response_model.NotificationResponseModel
import com.rolling.meadows.data.response_model.UserDetailResponseModel
import com.rolling.meadows.utils.ApiConstants
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Nikita kohli
 */
interface ApiService {
    /****************Authentication module**************************/
    @POST(ApiConstants.Authentication.LOGIN)
    suspend fun login(@Body value: LoginModel): Response<LoginUserDetailModel>

    @POST(ApiConstants.Authentication.VERIFY_OTP)
    suspend fun verifyOTP(@Body value: OTPVerificationModel): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.RESEND_VERIFY_OTP)
    suspend fun resendVerificationCOde(@Body value: OTPVerificationModel): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body value: ForgotPasswordModel): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.RESET_PASSWORD)
    suspend fun resetPassword(@Body value: ResetPasswordModel): Response<UserDetailResponseModel>

    @GET(ApiConstants.Authentication.LOGOUT)
    suspend fun logout(): Response<BaseResponseModel>

    @GET(ApiConstants.Authentication.GET_PROFILE)
    suspend fun getProfile(): Response<UserDetailResponseModel>

    @GET(ApiConstants.Notification.API_NOTIFICATION_LIST)
    suspend fun notificationList(
        @Query("page_limit") page_limit: Int,
        @Query("page") page: Int
    ): Response<NotificationResponseModel>

}