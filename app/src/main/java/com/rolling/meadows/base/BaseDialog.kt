package com.rolling.meadows.base

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.rolling.meadows.R


open class BaseDialog : Dialog, View.OnClickListener{
    var baseActivity: BaseActivity?


    constructor(context: Context) : super(context, R.style.animateDialog) {
        baseActivity = context as BaseActivity

    }

    constructor(context: BaseActivity, customDialog: Int) : super(context, customDialog) {
        baseActivity = context

    }


    override fun onClick(v: View) {
        hideKeyBoardDialog()
    }

    //hide keyboard on click outside the edittext
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = baseActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun hideKeyBoardDialog() {
        val im = baseActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (im != null && window != null) {
            im.hideSoftInputFromWindow(window?.decorView?.windowToken, 0)
        }
    }




    fun showToast(msg: String) {
        baseActivity?.showToast(msg)
    }


    fun setOnDialogClickListener(onDialogClickListener: OnDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener
    }

    private var onDialogClickListener: OnDialogClickListener? = null

    fun onDialogClick(vararg itemData: Any) {
        if (onDialogClickListener != null) {
            onDialogClickListener!!.onDialogClick(*itemData)
        }
    }

    interface OnDialogClickListener {
        fun onDialogClick(vararg itemData: Any)
    }


}

