package com.rolling.meadows.socket

import android.util.Log
import androidx.annotation.Nullable
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.rolling.meadows.cache.CacheConstants
import com.rolling.meadows.cache.Prefs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    var mSocket: Socket? = null
    val TAG = "socket"
    companion object{
        var socket: Socket? = null
    }

    fun connect() {
        if (mSocket == null) {
            initSocket()
        }

        if (mSocket?.connected() == true) return

        mSocket?.on(Socket.EVENT_CONNECT) {
            Log.e(TAG, "SOCKET_STATE.CONNECTED >>>> SocketId${mSocket?.id()}")
            currentSocketState.postValue(SOCKET_STATE.CONNECTED)
        }?.on(Socket.EVENT_DISCONNECT) {
            Log.e(TAG, "SOCKET_STATE.DISCONNECTED >>>> SocketId${mSocket?.id()}")
            currentSocketState.postValue(SOCKET_STATE.DISCONNECTED)
        }?.on(Socket.EVENT_ERROR) {
            Log.e(TAG, "SOCKET_STATE.ERROR >>>> SocketId${mSocket?.id()}")
            currentSocketState.postValue(SOCKET_STATE.ERROR)
        }?.on(Socket.EVENT_CONNECT_TIMEOUT) {
            Log.e(TAG, "SOCKET_STATE.TIMEOUT >>>> SocketId${mSocket?.id()}")
            currentSocketState.postValue(SOCKET_STATE.ERROR)
        }?.on(Socket.EVENT_RECONNECT_FAILED) {
            Log.e(TAG, "EVENT_RECONNECT_FAILED.TIMEOUT >>>> SocketId: ${it[0]}")
            currentSocketState.postValue(SOCKET_STATE.ERROR)
        }?.on(Socket.EVENT_RECONNECT) {
            Log.e(TAG, "EVENT_RECONNECT.TIMEOUT >>>> SocketId: ${it[0]}")
            currentSocketState.postValue(SOCKET_STATE.ERROR)
        }?.on(Socket.EVENT_RECONNECT_ATTEMPT) {
            Log.e(TAG, "SOCKET_STATE.TIMEOUT >>>> SocketId: ${it[0]}")
            currentSocketState.postValue(SOCKET_STATE.ERROR)
        }?.on(Socket.EVENT_RECONNECT_ERROR) {
            Log.e(TAG, "EVENT_RECONNECT_ERROR.TIMEOUT >>>> SocketId: ${it[0]}")
            currentSocketState.postValue(SOCKET_STATE.ERROR)
        }
        mSocket?.connect()
        socket = mSocket
    }

    private fun initSocket() {
        Log.e(TAG, "Initialize Socket")

        val opts = IO.Options().apply {
            forceNew = true
            query = "user_token=${getToken() }"//&user_id=${getUserId()}
        }
        mSocket = IO.socket(SocketEvents.SOCKET_URL,opts)
    }

    fun disconnect() {
        mSocket?.disconnect()
        mSocket = null
    }

    fun on(event: String, listener: Emitter.Listener) {
        mSocket?.on(event, listener)
    }

    fun emit(event: String, @Nullable obj: Any) {
        mSocket?.emit(event, obj)
    }

    fun emit(event: String, @Nullable obj: Any, @Nullable ack: Ack) {
        mSocket?.emit(event, obj, ack)
    }

    fun removeEvent(event: String) {
        mSocket?.off(event)
    }

    fun isConnected(): Boolean {
        return mSocket?.connected() ?: false
    }


/*
    fun clearSession() {
        mSocket?.off(SocketEvents.ON_RIDE_STATUS_UPDATE)
        mSocket?.off(Socket.EVENT_CONNECT)
        mSocket?.disconnect()
    }
*/

    enum class SOCKET_STATE {
        DISCONNECTED,
        CONNECTED,
        ERROR
    }


    private fun getToken(): String? {
        return Prefs.getString(CacheConstants.USER_TOKEN, "device_token")
    }
    private fun getUserId(): String? {
        return Prefs.getString(CacheConstants.USER_ID, "user_id")
    }
}