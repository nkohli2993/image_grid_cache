package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.response_model.EventDetailResponseModel
import com.rolling.meadows.data.response_model.EventResponseModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.network.retrofit.NetworkOnlineDataRepo
import com.rolling.meadows.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private var userRepository: UserRepository
) : BaseViewModel() {

    var filterBy: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {
            value = 0
        }
    var date: MutableLiveData<String> =
        MutableLiveData<String>().apply {
            value = ""
        }
    var endDate: MutableLiveData<String> =
        MutableLiveData<String>().apply {
            value = ""
        }
    var pageLimit: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {
            value = 20
        }
    var page: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {
            value = 1
        }
    var eventId: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {
            value = -1
        }

    private var _eventResponseLiveData =
        MutableLiveData<Event<DataResult<EventResponseModel>>>()

    var eventResponseLiveData: LiveData<Event<DataResult<EventResponseModel>>> =
        _eventResponseLiveData

    private var _eventDetailResponseLiveData =
        MutableLiveData<Event<DataResult<EventDetailResponseModel>>>()

    var eventDetailResponseLiveData: LiveData<Event<DataResult<EventDetailResponseModel>>> =
        _eventDetailResponseLiveData

    private var _logoutResponseLiveData =
        MutableLiveData<Event<DataResult<BaseResponseModel>>>()


    var logoutResponseLiveData: LiveData<Event<DataResult<BaseResponseModel>>> =
        _logoutResponseLiveData

     fun hitLogOutApi() {
        viewModelScope.launch {
            val response =
                eventRepository._logout()
            withContext(Dispatchers.Main) {
                response.collect { _logoutResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    fun hitEventApi() {
        viewModelScope.launch {
            val response =
                eventRepository.eventList(
                    filterBy.value!!,
                    date.value!!,
                    endDate.value!!,
                    pageLimit.value!!,
                    page.value!!
                )
            withContext(Dispatchers.Main) {
                response.collect { _eventResponseLiveData.postValue(Event(it)) }
            }
        }
    }
    fun hitEventDetailApi() {
        viewModelScope.launch {
            val response =
                eventRepository.eventDetail(
                    eventId.value!!
                )
            withContext(Dispatchers.Main) {
                response.collect { _eventDetailResponseLiveData.postValue(Event(it)) }
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