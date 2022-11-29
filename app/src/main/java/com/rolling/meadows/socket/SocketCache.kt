package com.rolling.meadows.socket

import androidx.lifecycle.MutableLiveData


val currentSocketState = MutableLiveData<SocketManager.SOCKET_STATE>()
    .apply {
        postValue(SocketManager.SOCKET_STATE.DISCONNECTED)
    }