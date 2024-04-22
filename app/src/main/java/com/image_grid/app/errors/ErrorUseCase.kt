package com.image_grid.app.errors


interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
