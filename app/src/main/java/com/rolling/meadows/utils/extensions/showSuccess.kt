package com.rolling.meadows.utils.extensions

import android.content.Context
import es.dmoral.toasty.Toasty

/**
 * Created by Nikita kohli 06/10/2022
 */

fun showSuccess(context: Context, msg: String) {
    Toasty.success(context, msg).show()
}

fun showError(context: Context, msg: String) {
    Toasty.error(context, msg).show()
}

fun showInfo(context: Context, msg: String) {
    Toasty.info(context, msg).show()
}

fun showWarning(context: Context, msg: String) {
    Toasty.warning(context, msg).show()
}
