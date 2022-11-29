package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.ForgotPasswordModel
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
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    var forgotPasswordLiveData: MutableLiveData<ForgotPasswordModel> =
        MutableLiveData<ForgotPasswordModel>().apply {
            value = ForgotPasswordModel()
        }

    private var _forgotPasswordResponseLiveData =
        MutableLiveData<Event<DataResult<UserDetailResponseModel>>>()


    var forgotPasswordResponseLiveData: LiveData<Event<DataResult<UserDetailResponseModel>>> =
        _forgotPasswordResponseLiveData

    fun onClickForgotPassword() {
        forgotPasswordLiveData.value?.let {
            if (it.isValid()) {
                hitOtpVerification()

            } else {
                it.notifyChange()
            }
        }
    }

    private fun hitOtpVerification() {
        viewModelScope.launch {
            val response =
                forgotPasswordLiveData.value?.let { authRepository.forgotPassword(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _forgotPasswordResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    // Save User to SharePrefs
    fun saveUser(user: UserProfileData?) {
        userRepository.saveUser(user)
    }

}