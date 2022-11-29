package com.rolling.meadows.socket

import android.util.Log
import com.rolling.meadows.base.BaseError
import org.json.JSONArray
import org.json.JSONObject

fun isSocketResponseValid(jsonArray: JSONArray): Pair<JSONObject?, BaseError?> {
    Log.e("response", "" + jsonArray[0])

    return try {
        val status = jsonArray[0] as String
        val baseError = BaseError()
        baseError.message = status
        baseError.code = 500
        Pair(null, baseError)
    }catch (e:Exception){
        val baseError = BaseError()
        baseError.message = "No result found"
        baseError.code = 500
        Pair(null, baseError)
    }
/*
    try {
        val jsonObject = jsonArray[0] as JSONObject

        return if (jsonObject.getInt("statusCode") == 200) {
            val jsonObject = jsonArray[0] as JSONObject
            Pair(jsonObject, null)
        } else {
            val baseError = BaseError()
            val jsonObject = jsonArray[0] as JSONObject
            baseError.message = jsonObject.getString("message")
            baseError.code = jsonObject.optInt("statusCode")
            Pair(null, baseError)
        }
    } catch (e: Exception) {
        val baseError = BaseError()
        baseError.message = "No result found"
        baseError.code = 500
        return Pair(null, baseError)
    }
*/
}


fun isSocketResponseSuccess(jsonObject: JSONObject): Boolean {
    if (jsonObject.getBoolean("status")) {
        return true
    }

    return false
}