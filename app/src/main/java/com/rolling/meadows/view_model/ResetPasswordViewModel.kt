package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.ResetPasswordModel
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.response_model.UserDetailResponseModel
import com.rolling.meadows.data.static_pages.ChangePasswordModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository, private var userRepository: UserRepository
) : BaseViewModel() {


    var resetPasswordLiveData: MutableLiveData<ResetPasswordModel> =
        MutableLiveData<ResetPasswordModel>().apply {
            value = ResetPasswordModel()
        }

    private var _resetPasswordResponseLiveData =
        MutableLiveData<Event<DataResult<UserDetailResponseModel>>>()


    var resetPasswordResponseLiveData: LiveData<Event<DataResult<UserDetailResponseModel>>> =
        _resetPasswordResponseLiveData

    var changePasswordLiveData: MutableLiveData<ChangePasswordModel> =
        MutableLiveData<ChangePasswordModel>().apply {
            value = ChangePasswordModel()
        }

    private var _changePasswordResponseLiveData =
        MutableLiveData<Event<DataResult<UserDetailResponseModel>>>()


    var changePasswordResponseLiveData: LiveData<Event<DataResult<UserDetailResponseModel>>> =
        _changePasswordResponseLiveData

    fun onClickResetPassword() {
        resetPasswordLiveData.value?.let {
            if (it.isValid()) {
                hitResetPassword()

            } else {
                it.notifyChange()
            }
        }
    }

     fun hitResetPassword() {
        viewModelScope.launch {
            val response =
                resetPasswordLiveData.value?.let { authRepository.resetPassword(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _resetPasswordResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    // Save User to SharePrefs
    fun saveUser(user: UserProfileData?) {
        userRepository.saveUser(user)
    }

    // Save User to SharePrefs
    fun getSaveUser(): UserProfileData? {
        return userRepository.getUser()
    }

    // Save User token to SharePrefs
    fun saveToken(token: String?) {
        userRepository.saveToken(token)
    }


    fun clearAllData() {
        userRepository.clearAllData()
    }
}