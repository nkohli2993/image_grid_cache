package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.OTPVerificationModel
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private var userRepository: UserRepository
) : BaseViewModel() {


    var logoutLiveData: MutableLiveData<OTPVerificationModel> =
        MutableLiveData<OTPVerificationModel>().apply {
            value = OTPVerificationModel()
        }

    private var _logoutResponseLiveData =
        MutableLiveData<Event<DataResult<BaseResponseModel>>>()


    var logoutResponseLiveData: LiveData<Event<DataResult<BaseResponseModel>>> =
        _logoutResponseLiveData

    fun onClickLogout() {
        hitLogOutApi()
    }

    private fun hitLogOutApi() {
        viewModelScope.launch {
            val response =
                authRepository._logout()
            withContext(Dispatchers.Main) {
                response.collect { _logoutResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    // Save User to SharePrefs
    fun getSavedUser(): UserProfileData? {
        return userRepository.getUser()
    }

    fun saveToken(token: String?) {
        userRepository.saveToken(token)
    }

    fun clearAllData() {
        userRepository.clearAllData()
    }
}