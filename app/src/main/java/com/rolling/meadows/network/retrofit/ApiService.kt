package com.rolling.meadows.network.retrofit

import com.rolling.meadows.data.FavoriteResponseModel
import com.rolling.meadows.data.authentication.*
import com.rolling.meadows.data.booking.request.CancelRideData
import com.rolling.meadows.data.booking.request.PaymentRequest
import com.rolling.meadows.data.booking.request.RatingRequest
import com.rolling.meadows.data.booking.request.RideRequest
import com.rolling.meadows.data.booking.response.*
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.response_model.LoginUserDetailModel
import com.rolling.meadows.data.response_model.NotificationResponseModel
import com.rolling.meadows.data.response_model.UserDetailResponseModel
import com.rolling.meadows.data.static_pages.ChangePasswordModel
import com.rolling.meadows.data.static_pages.ContactUsModel
import com.rolling.meadows.data.static_pages.StaticPageResponseModel
import com.rolling.meadows.utils.ApiConstants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created byNikita kohli on 20/09/22
 */
interface ApiService {
    /****************Authentication module**************************/
    @POST(ApiConstants.Authentication.LOGIN)
    suspend fun login(@Body value: LoginModel): Response<LoginUserDetailModel>

    @Multipart
    @POST(ApiConstants.Authentication.REGISTER)
    suspend fun registerUser(
        @Part image: MultipartBody.Part?,
        @Part("email") email: RequestBody?,
        @Part("first_name") firstName: RequestBody?,
        @Part("last_name") lastName: RequestBody?,
        @Part("iso_code") iso_code: RequestBody?,
        @Part("phone_code") phone_code: RequestBody?,
        @Part("phone_number") phone_number: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("fcm_token") fcm_token: RequestBody?,
        @Part("device_type") device_type: RequestBody?,
        @Part("role") role: RequestBody?
    ): Response<UserDetailResponseModel>

    @Multipart
    @POST(ApiConstants.Authentication.REGISTER)
    suspend fun registerUserWithoutEmail(
        @Part image: MultipartBody.Part?,
        @Part("first_name") firstName: RequestBody?,
        @Part("last_name") lastName: RequestBody?,
        @Part("iso_code") iso_code: RequestBody?,
        @Part("phone_code") phone_code: RequestBody?,
        @Part("phone_number") phone_number: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("fcm_token") fcm_token: RequestBody?,
        @Part("device_type") device_type: RequestBody?,
        @Part("role") role: RequestBody?
    ): Response<UserDetailResponseModel>

    @Multipart
    @POST(ApiConstants.Authentication.UPDATE_PROFILE)
    suspend fun updateProfile(
        @Part image: MultipartBody.Part?,
        @Part("first_name") firstName: RequestBody?,
        @Part("last_name") lastName: RequestBody?,
        @Part("email") email: RequestBody?,
    ): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.VERIFY_OTP)
    suspend fun verifyPhoneNumber(@Body value: OTPVerificationModel): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.RESET_VERIFY_OTP)
    suspend fun resetVerifyPhoneNumber(@Body value: OTPVerificationModel): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.RESEND_CODE)
    suspend fun resendCode(@Body value: ResendOTPModel): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body value: ForgotPasswordModel): Response<UserDetailResponseModel>

    @POST(ApiConstants.Authentication.RESET_PASSWORD)
    suspend fun resetPassword(@Body value: ResetPasswordModel): Response<UserDetailResponseModel>

    @GET(ApiConstants.Authentication.LOGOUT)
    suspend fun logout(): Response<BaseResponseModel>

    @GET(ApiConstants.Authentication.GET_PROFILE)
    suspend fun getProfile(): Response<UserDetailResponseModel>

    /**************** Static pages **************************/
    @POST(ApiConstants.Pages.CONTACT_US)
    suspend fun contactUs(@Body value: ContactUsModel): Response<BaseResponseModel>

    @GET(ApiConstants.Pages.STATIC_PAGES)
    suspend fun getStaticPageInfo(@Query("page_type") page_type: String): Response<StaticPageResponseModel>

    @POST(ApiConstants.Pages.CHANGE_PASSWORD)
    suspend fun changePassword(@Body value: ChangePasswordModel): Response<UserDetailResponseModel>

    /****************  Ride **************************/
    @POST(ApiConstants.ride.BOOK_RIDE)
    suspend fun bookRide(@Body value: RideRequest): Response<BookingRideResponseModel>

    @POST(ApiConstants.ride.GET_RIDE_AMOUNT)
    suspend fun getRideAmount(@Body value: RideRequest): Response<RideAmountResponse>

    @GET(ApiConstants.ride.SEARCH_RIDE_VEHICLE)
    suspend fun searchRide(
        @Query("pickup_location") pickup_location: String,
        @Query("drop_location") drop_location: String,
        @Query("pickup_latitude") pickup_latitude: String,
        @Query("pickup_longitude") pickup_longitude: String,
        @Query("drop_latitude") drop_latitude: String,
        @Query("drop_longitude") drop_longitude: String,
    ): Response<VehiclePayload>

    @GET(ApiConstants.ride.RECENT_SEARCH_LIST)
    suspend fun recentSearchHistory(
        @Query("page") page: Int,
        @Query("page_limit") page_limit: Int,
    ): Response<RecentSearchHistoryResponseModel>


    @GET(ApiConstants.ride.RETRY_RIDE_REQUEST)
    suspend fun retryRideRequest(): Response<BaseResponseModel>


    @POST(ApiConstants.ride.PAST_RIDE)
    suspend fun pastRide(
        @Query("page") page: Int,
        @Query("ride_tab") ride_tab: Int,
    ): Response<MyRideRespose>


    @POST(ApiConstants.payment.PAY_TIP)
    suspend fun addTip(@Body value: PaymentRequest): Response<BaseResponseModel>


    /**************** Payment **************************/
    @POST(ApiConstants.payment.ADD_CARD)
    suspend fun addCard(@Body value: PaymentRequest): Response<BaseResponseModel>

    @POST(ApiConstants.payment.CHECKOUT)
    suspend fun checkout(@Body value: PaymentRequest): Response<BaseResponseModel>

    @POST(ApiConstants.ride.BOOK_SCHEDULE_RIDE)
    suspend fun bookScheduleRide(@Body value: PaymentRequest): Response<BaseResponseModel>


    @GET(ApiConstants.payment.CARD_LISTING)
    suspend fun cardList(): Response<CardListResponse>


    @POST(ApiConstants.payment.REMOVE_CARD)
    suspend fun removeCard(@Query("card_id") id: Int): Response<BaseResponseModel>

    @GET(ApiConstants.ride.MY_RIDE)
    suspend fun myRides(
        @Query("page_limit") page_limit: Int,
        @Query("page") page: Int,
        @Query("ride_tab") ride_tab: Int
    ): Response<MyBookingResponseModel>

    @GET(ApiConstants.ride.RIDE_DETAIL)
    suspend fun rideDetail(
        @Query("ride_id") ride_id: Int,
    ): Response<BookingRideResponseModel>

    @GET(ApiConstants.ride.PAYMENT_SELECTION_METHOD_UPDATE)
    suspend fun paymentMethod(
        @Query("ride_id") ride_id: Int, @Query("payment_method") payment_method: Int
    ): Response<BookingRideResponseModel>

    @POST(ApiConstants.ride.API_CANCEL_RIDE)
    suspend fun cancelRide(
        @Body value: CancelRideData
    ): Response<BookingRideResponseModel>

    @POST(ApiConstants.ride.API_ADD_RIDER_RATING)
    suspend fun driverRating(
        @Body value: RatingRequest
    ): Response<BookingRideResponseModel>

    @GET(ApiConstants.Notification.API_NOTIFICATION_LIST)
    suspend fun notificationList(
        @Query("page_limit") page_limit: Int,
        @Query("page") page: Int
    ): Response<NotificationResponseModel>

    @GET(ApiConstants.Favorite.API_FAVORITE_LIST)
    suspend fun favoriteList(
        @Query("page_limit") page_limit: Int,
        @Query("page") page: Int
    ): Response<FavoriteResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.Favorite.API_ADD_FAVORITE_LIST)
    suspend fun addDriverFav(
       @Field("driver_id") driver_id:Int
    ): Response<FavoriteResponseModel>

    @FormUrlEncoded
    @POST(ApiConstants.Favorite.API_REMOVE_FAVORITE_LIST)
    suspend fun removeDriverFav(
       @Field("driver_id") driver_id:Int
    ): Response<FavoriteResponseModel>

}