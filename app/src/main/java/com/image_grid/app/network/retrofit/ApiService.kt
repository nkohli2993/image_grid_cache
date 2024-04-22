package com.image_grid.app.network.retrofit

import com.image_grid.app.ApiConstants
import com.image_grid.app.data.ImageData
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Nikita kohli
 */
interface ApiService {

    @GET(ApiConstants.LIST_SHOW)
    suspend fun getImageList(@Query("limit") limit:Int): Response<ArrayList<ImageData>>
//    suspend fun getImageList(@Query("limit") limit:Int): Response<ArrayList<ImageData>>

}