package com.rolling.meadows.view_model

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.authentication.registration.RegisterModel
import com.rolling.meadows.data.response_model.UserDetailResponseModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.repository.AuthenticationRepository
import com.rolling.meadows.utils.extensions.showError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by  Nikita Kohli 04/10/2022
 */

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    var registrationLiveData: MutableLiveData<RegisterModel> =
        MutableLiveData<RegisterModel>().apply {
            value = RegisterModel()
        }

    private var _registerResponseLiveData =
        MutableLiveData<Event<DataResult<UserDetailResponseModel>>>()

    var registerResponseLiveData: LiveData<Event<DataResult<UserDetailResponseModel>>> =
        _registerResponseLiveData

    fun onClickRegister(view: View) {
        registrationLiveData.value?.let {
            if (it.isValid()) {
                hitRegistration()

            } else {
                if (it.profileFile.isNullOrEmpty()) {
                    showError(
                        view.context,
                        view.context.getString(R.string.plz_select_profile_picture)
                    )
                } else {
                    it.notifyChange()
                }
            }
        }
    }

    private fun hitRegistration() {
        viewModelScope.launch {
            val response = registrationLiveData.value?.let {
                if (it.email?.trim() != null) {
                    authRepository.registerUser(it)
                } else {
                    authRepository.registerUserWithoutEmail(it)
                }
            }
            withContext(Dispatchers.Main) {
                response?.collect { _registerResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    // Save User to SharePrefs
    fun saveToken(token: String?) {
        userRepository.saveToken(token)
    }

    // Save User to SharePrefs
    fun saveUser(user: UserProfileData?) {
        userRepository.saveUser(user)
    }
    fun saveId(id: String?) {
        userRepository.saveUserId(id)
    }

    // get token to SharePrefs
    fun getDeviceToken(): String? {
        return userRepository.getDeviceToken()
    }
}