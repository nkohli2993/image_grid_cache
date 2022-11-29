package com.rolling.meadows.base

import androidx.lifecycle.ViewModel
import com.rolling.meadows.errors.ErrorManager
import javax.inject.Inject

/**
 * Created by Nikita kohli 20/09/2022
 */

abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
}
