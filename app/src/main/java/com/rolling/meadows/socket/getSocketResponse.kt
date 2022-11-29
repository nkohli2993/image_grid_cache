package com.rolling.meadows.socket

import android.util.Log
import com.google.gson.Gson
import com.rolling.meadows.utils.extensions.showLog
import org.json.JSONArray
import org.json.JSONObject

fun <T : Any> getSocketResponse(it: Array<Any>, dataType: Class<T>): SocketResource<T> {
    try {
        Log.i("Socket Response", "Data : ${Gson().toJson(it)}")
        val response = JSONArray(it)
        val socketResponse = JSONObject(response[response.length() - 1] as String)
        val statusCode: Int = socketResponse.getInt("status")


        if ((statusCode >= 200 && statusCode <= 202) || (statusCode >= 320 && statusCode <= 330)) {
            val data: T = Gson().fromJson("$socketResponse", dataType)
            return SocketResource.success(data)
        } else {
            val msg = socketResponse.getString("message")
            return SocketResource.error(msg, null)
        }
    } catch (e: Exception) {
        showLog("Socket_Error :", message =e.message.toString())
        return SocketResource.error("${e.message}", null)
    }
}

