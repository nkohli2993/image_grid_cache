package com.rolling.meadows.errors

import com.rolling.meadows.data.error.mapper.ErrorMapper
import javax.inject.Inject

/**
 * Created by Nikita kohli 20/09/2022
 */


class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}
