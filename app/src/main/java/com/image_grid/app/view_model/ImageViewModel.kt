package com.image_grid.app.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.image_grid.app.base.BaseViewModel
import com.image_grid.app.data.ImageData
import com.image_grid.app.network.retrofit.DataResult
import com.image_grid.app.network.retrofit.Event
import com.image_grid.app.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ImageViewModel  @Inject constructor(
    private val imagepository: ImageRepository,
) : BaseViewModel() {


    var limit: MutableLiveData<Int> =
        MutableLiveData<Int>().apply {
            value = 20
        }

    private var _imageResponseLiveData =
        MutableLiveData<Event<DataResult<ArrayList<ImageData>>>>()


    var imageResponseLiveData: LiveData<Event<DataResult<ArrayList<ImageData>>>> =
        _imageResponseLiveData


    fun getImageList() {
        viewModelScope.launch {
            val response =
                imagepository.getListImages(limit.value!!)
            withContext(Dispatchers.Main) {
                response.collect { _imageResponseLiveData.postValue(Event(it)) }
            }
        }
    }
  }