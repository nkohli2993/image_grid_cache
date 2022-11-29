package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.OTPVerificationModel
import com.rolling.meadows.data.authentication.ResendOTPModel
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.response_model.UserDetailResponseModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private var userRepository: UserRepository
) : BaseViewModel() {


    var otpVerificationData = MutableLiveData<UserDetailResponseModel>().apply {
        value = UserDetailResponseModel()
    }

    var otpVerificationLiveData: MutableLiveData<OTPVerificationModel> =
        MutableLiveData<OTPVerificationModel>().apply {
            value = OTPVerificationModel()
        }
    var resendOtpVerificationLiveData: MutableLiveData<ResendOTPModel> =
        MutableLiveData<ResendOTPModel>().apply {
            value = ResendOTPModel()
        }

    private var _otpVerificationResponseLiveData =
        MutableLiveData<Event<DataResult<UserDetailResponseModel>>>()

    private var _resendOtpVerificationResponseLiveData =
        MutableLiveData<Event<DataResult<UserDetailResponseModel>>>()


    var resendOtpVerificationLiveDataResponse: LiveData<Event<DataResult<UserDetailResponseModel>>> =
        _resendOtpVerificationResponseLiveData


    var otpVerificationResponseLiveData: LiveData<Event<DataResult<UserDetailResponseModel>>> =
        _otpVerificationResponseLiveData

    fun onClickOtpVerify() {
        otpVerificationLiveData.value?.let {
            if (it.isValid()) {
                hitOtpVerification()

            } else {
                it.notifyChange()
            }
        }
    }
    fun onClickResetOtpVerify() {
        otpVerificationLiveData.value?.let {
            if (it.isValid()) {
                hitResetOtpVerification()

            } else {
                it.notifyChange()
            }
        }
    }

    private fun hitOtpVerification() {
        viewModelScope.launch {
            val response =
                otpVerificationLiveData.value?.let { authRepository.verifyPhoneNumber(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _otpVerificationResponseLiveData.postValue(Event(it)) }
            }
        }
    }
    private fun hitResetOtpVerification() {
        viewModelScope.launch {
            val response =
                otpVerificationLiveData.value?.let { authRepository.resetVerifyPhoneNumber(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _otpVerificationResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    fun hitResendOtpVerification() {
        viewModelScope.launch {
            val response =
                resendOtpVerificationLiveData.value?.let { authRepository.resendPhoneNUmberOtp(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _resendOtpVerificationResponseLiveData.postValue(Event(it)) }
            }
        }
    }


    fun getUser(): UserProfileData? {
        return userRepository.getUser()
    }

    // Save User to SharePrefs
    fun saveToken(token: String?) {
        userRepository.saveToken(token)
    }

    // Save User to SharePrefs
    fun saveUser(user: UserProfileData?) {
        userRepository.saveUser(user)
    }

}