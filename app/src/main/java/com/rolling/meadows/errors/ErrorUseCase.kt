package com.rolling.meadows.errors


interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
