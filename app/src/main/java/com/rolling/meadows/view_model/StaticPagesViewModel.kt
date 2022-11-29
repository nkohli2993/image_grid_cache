package com.rolling.meadows.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rolling.meadows.base.BaseViewModel
import com.rolling.meadows.cache.UserRepository
import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.data.response_model.BaseResponseModel
import com.rolling.meadows.data.static_pages.ContactUsModel
import com.rolling.meadows.data.static_pages.StaticPageResponseModel
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.Event
import com.rolling.meadows.repository.StaticPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StaticPagesViewModel @Inject constructor(
    private val repository: StaticPageRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    var type: MutableLiveData<String> = MutableLiveData<String>().apply {
        value = String()
    }

    var contactUsLiveData: MutableLiveData<ContactUsModel> =
        MutableLiveData<ContactUsModel>().apply {
            value = ContactUsModel()
        }

    private var _contactUsResponseLiveData =
        MutableLiveData<Event<DataResult<BaseResponseModel>>>()


    var contactUsResponseLiveData: LiveData<Event<DataResult<BaseResponseModel>>> =
        _contactUsResponseLiveData

    private var _staticPageResponseLiveData =
        MutableLiveData<Event<DataResult<StaticPageResponseModel>>>()


    var staticPageResponseLiveData: LiveData<Event<DataResult<StaticPageResponseModel>>> =
        _staticPageResponseLiveData

    fun onClickContactUs() {
        contactUsLiveData.value?.let {
            if (it.isValid()) {
                hitContactUsApi()

            } else {
                it.notifyChange()
            }
        }
    }

    private fun hitContactUsApi() {
        viewModelScope.launch {
            val response =
                contactUsLiveData.value?.let { repository.contactUs(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _contactUsResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    fun hitStaticPageApi() {
        viewModelScope.launch {
            val response =
                type.value?.let { repository.getStaticPageInfo(it) }
            withContext(Dispatchers.Main) {
                response?.collect { _staticPageResponseLiveData.postValue(Event(it)) }
            }
        }
    }

    // get User to SharePrefs
    fun getUser(): UserProfileData? {
        return userRepository.getUser()
    }
}