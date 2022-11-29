package com.rolling.meadows.cache

import android.content.Context
import android.content.SharedPreferences
import com.rolling.meadows.di.provideAppContext
import com.google.gson.Gson

object Prefs {
    private val TAG = "Prefs"
    var preferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    init {
        preferences =
            provideAppContext().getSharedPreferences(TAG, Context.MODE_PRIVATE)
        editor = preferences?.edit()

    }

    fun save(key: String?, value: Boolean) {
        editor?.putBoolean(key, value)?.apply()
    }

    fun save(key: String?, value: String?) {
        editor?.putString(key, value)?.apply()
    }

    fun save(key: String?, value: Int) {
        editor?.putInt(key, value)?.apply()
    }

    fun save(key: String?, value: Float) {
        editor?.putFloat(key, value)?.apply()
    }

    fun save(key: String?, value: Long) {
        editor?.putLong(key, value)?.apply()
    }

    // to save object in prefrence
    fun save(key: String?, data: Any?) {
        editor!!.putString(key, Gson().toJson(data)).apply()
    }

    // to save object in prefrence
    fun saveRideData(key: String?, data: Any?) {
        editor!!.putString(key, Gson().toJson(data)).apply()
    }


    // To get object from prefrences
    fun <T> getObject(key: String, a: Class<T>?): T? {
        val gson = preferences!!.getString(key, null)
        return if (gson == null) {
            null
        } else {
            try {
                Gson().fromJson(gson, a)
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "Object storaged with key "
                            + key + " is instanceof other class"
                )
            }
        }
    }


    fun getBoolean(key: String?, defValue: Boolean? = false): Boolean? {
        return preferences?.getBoolean(key, defValue ?: false)
    }

    fun getString(key: String?, defValue: String? = ""): String? {
        return preferences?.getString(key, defValue)
    }

    fun getInt(key: String?, defValue: Int? = 0): Int? {
        return preferences?.getInt(key, defValue ?: 0)
    }

    fun getFloat(key: String?, defValue: Float? = 0f): Float? {
        return preferences?.getFloat(key, defValue ?: 0f)
    }

    fun getLong(key: String?, defValue: Long? = 0): Long? {
        return preferences?.getLong(key, defValue ?: 0)
    }


    fun removeAll() {
        editor?.clear()
        editor?.apply()
    }


}