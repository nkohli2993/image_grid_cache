package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.LoginModel
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.response_model.LoginUserDetailModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by  Nikita Kohli
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository, private var userRepository: UserRepository
) : BaseViewModel() {


    var loginData = MutableLiveData<LoginUserDetailModel>().apply {
        value = LoginUserDetailModel()
    }

    var loginLiveData: MutableLiveData<LoginModel> = MutableLiveData<LoginModel>().apply {
        value = LoginModel()
    }

    private var _loginResponseLiveData = MutableLiveData<Event<DataResult<LoginUserDetailModel>>>()

    var loginResponseLiveData: LiveData<Event<DataResult<LoginUserDetailModel>>> =
        _loginResponseLiveData

    fun onClickLogin() {
        saveToken(null)
        loginLiveData.value?.let {
            if (it.isValid()) {
                hitLogin()
            } else {
                it.notifyChange()
            }
        }
    }

    fun hitLogin() {
        viewModelScope.launch {
//            loginLiveData.value?.fcmToken = getDeviceToken()
            loginLiveData.value?.fcmToken = "fcm_token"
            val response = loginLiveData.value?.let { authRepository.loginUser(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _loginResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    // Save User to SharePrefs
    fun saveUser(user: UserProfileData?) {
        userRepository.saveUser(user)
    }

    // Save User to SharePrefs
    fun saveToken(token: String?) {
        userRepository.saveToken(token)
    }

    fun saveId(id: String?) {
        userRepository.saveUserId(id)
    }

    // get token to SharePrefs
    fun getDeviceToken(): String? {
        return userRepository.getDeviceToken()
    }

    fun saveDeviceToken(token: String?) {
        userRepository.saveDeviceToken(token)
    }
}