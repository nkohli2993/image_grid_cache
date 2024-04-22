package com.image_grid.app.repository

import android.media.Image
import com.image_grid.app.data.ImageData
import com.image_grid.app.network.retrofit.ApiService
import com.image_grid.app.network.retrofit.DataResult
import com.image_grid.app.network.retrofit.NetworkOnlineDataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ImageRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListImages(value: Int): Flow<DataResult<ArrayList<ImageData>>> {
        return object : NetworkOnlineDataRepo<ArrayList<ImageData>, ArrayList<ImageData>>() {
            override suspend fun fetchDataFromRemoteSource(): Response<ArrayList<ImageData>> {
                return apiService.getImageList(value)
            }
        }.asFlow().flowOn(Dispatchers.IO)
    }
}