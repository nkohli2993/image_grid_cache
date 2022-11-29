package com.rolling.meadows.network.retrofit

import androidx.annotation.MainThread
import com.google.gson.Gson
import com.rolling.meadows.utils.extensions.showException
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

abstract class NetworkOnlineDataRepo<RESULT, REQUEST> {
    fun asFlow() = flow {
        emit(DataResult.Loading())
        try {
            val apiResponse = fetchDataFromRemoteSource()
            val data = apiResponse.body()

            if (apiResponse.isSuccessful && data != null) {
                /* if (getResponseCode(data) == HTTPStatus.STATUS_SUCCESS) {
                     emit(DataResult.Success(data))
                 } else {
                     emit(
                         DataResult.Failure(
                             getErrorMsg(data),
                             errorCode = getResponseCode(data)
                         )
                     )

                 }*/
                emit(DataResult.Success(data))
            } else {

                emit(
                    DataResult.Failure(
                        getErrorMsg(apiResponse.errorBody()!!),
                        errorCode = apiResponse.code()
                    )
                )

            }
        } catch (e: Exception) {
            emit(
                DataResult.Failure(
                    e.message, e
                )
            )
        }

    }

    private fun getResponseCode(data: REQUEST): Int {
        try {
            val jsonObject = JSONObject(Gson().toJson(data))

            return if (jsonObject.has("status")) {
                jsonObject.getInt("status")
            } else 200

        } catch (e: Exception) {
            showException(e)
        }
        return 0
    }

    fun getErrorMsg(responseBody: ResponseBody): String {

        try {
            val jsonObject = JSONObject(responseBody.string())

            return jsonObject.getString("message")

        } catch (e: Exception) {
            return e.message!!
        }

    }

    fun getErrorMsg(responseBody: REQUEST): String {

        try {
            val jsonObject = JSONObject(Gson().toJson(responseBody))

            return jsonObject.getString("message")

        } catch (e: Exception) {
            return e.message!!
        }

    }

    @MainThread
    protected abstract suspend fun fetchDataFromRemoteSource(): Response<REQUEST>
}