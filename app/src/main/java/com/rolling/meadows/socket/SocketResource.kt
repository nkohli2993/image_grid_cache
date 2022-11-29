package com.rolling.meadows.socket
data class SocketResource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): SocketResource<T> {
            return SocketResource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): SocketResource<T> {
            return SocketResource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): SocketResource<T> {
            return SocketResource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS, ERROR, LOADING
}