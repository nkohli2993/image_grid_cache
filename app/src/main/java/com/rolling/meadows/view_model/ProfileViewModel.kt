package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.EditProfileModel
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
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository, private var userRepository: UserRepository
) : BaseViewModel() {

    var userProfileData = MutableLiveData<EditProfileModel>().apply {
        value = EditProfileModel()
    }

    private var _profileResponseLiveData =
        MutableLiveData<Event<DataResult<UserDetailResponseModel>>>()

    var profileResponseLiveData: LiveData<Event<DataResult<UserDetailResponseModel>>> =
        _profileResponseLiveData

    fun getProfile() {
        viewModelScope.launch {
            val response = authRepository.getProfile()
            withContext(Dispatchers.Main) {
                response.collect { _profileResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    fun onClickUpdateProfile() {
        userProfileData.value.let {
            updateUserProfile()
        }
    }

    private fun updateUserProfile() {
        viewModelScope.launch {
            val response = userProfileData.value.let { authRepository.updateUserProfile(it!!) }
            withContext(Dispatchers.Main) {
                response.collect { _profileResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    // Save User to SharePrefs
    fun saveUser(user: UserProfileData?) {
        userRepository.saveUser(user)
    }
    fun saveId(id: String?) {
        userRepository.saveUserId(id)
    }
    // Save User to SharePrefs
    fun getSavedUser(): UserProfileData? {
        return userRepository.getUser()
    }

    // Save User token to SharePrefs
    fun saveToken(token: String?) {
        userRepository.saveToken(token)
    }

    // get User token to SharePrefs
    fun getToken(): String? {
        return userRepository.getToken()
    }

    // get token to SharePrefs
    fun getDeviceToken(): String? {
        return userRepository.getDeviceToken()
    }
}