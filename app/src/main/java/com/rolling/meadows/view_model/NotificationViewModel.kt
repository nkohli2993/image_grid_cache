package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.response_model.NotificationResponseModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel  @Inject constructor(
    private val notiRepository: NotificationRepository,
    private var userRepository: UserRepository
) : BaseViewModel() {


    var page: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {
            value = 1
        }

    var limit: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {
            value = 20
        }

    private var _notificationResponseLiveData =
        MutableLiveData<Event<DataResult<NotificationResponseModel>>>()


    var notificationResponseLiveData: LiveData<Event<DataResult<NotificationResponseModel>>> =
        _notificationResponseLiveData


    fun hitNotificationApi() {
        viewModelScope.launch {
            val response =
                notiRepository.getNotificationList(page.value!!,limit.value!!)
            withContext(Dispatchers.Main) {
                response.collect { _notificationResponseLiveData.postValue(Event(it)) }
            }
        }
    }


    fun saveToken(token: String?) {
        userRepository.saveToken(token)
    }


    fun clearAllData() {
        userRepository.clearAllData()
    }

    fun getUser(): UserProfileData? {
        return userRepository.getUser()
    }

}