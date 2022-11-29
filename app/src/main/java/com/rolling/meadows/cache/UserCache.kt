package com.rolling.meadows.cache

import com.rolling.meadows.data.authentication.UserProfileData
import com.rolling.meadows.network.retrofit.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {

    fun getToken(): String? {
        return Prefs.getString(CacheConstants.USER_TOKEN, "")
    }

    fun saveToken(token: String?) {
        Prefs.save(CacheConstants.USER_TOKEN, token)
    }

    fun getUserId(): String? {
        return Prefs.getString(CacheConstants.USER_ID, "")
    }

    fun saveUserId(user_id: String?) {
        Prefs.save(CacheConstants.USER_ID, user_id)
    }

    fun getDeviceToken(): String? {
        return Prefs.getString(CacheConstants.DEVICE_TOKEN, "")
    }

    fun saveDeviceToken(token: String?) {
        Prefs.save(CacheConstants.DEVICE_TOKEN, token)
    }

    fun getUser(): UserProfileData? {
        return Prefs.getObject(CacheConstants.USER_DATA, UserProfileData::class.java)
    }

    fun saveUser(user: UserProfileData?) {
        Prefs.save(CacheConstants.USER_DATA, user)
    }

    fun clearAllData() {
        Prefs.removeAll()
    }

}
