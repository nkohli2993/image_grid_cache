package com.image_grid.app.errors

import com.image_grid.app.data.error.mapper.ErrorMapper
import javax.inject.Inject

/**
 * Created by Nikita kohli
 */


class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}
