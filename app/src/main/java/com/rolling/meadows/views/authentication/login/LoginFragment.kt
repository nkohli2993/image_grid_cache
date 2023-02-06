package com.rolling.meadows.views.authentication.login

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.cache.Prefs
import com.rolling.meadows.databinding.FragmentLoginBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.view_model.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.annotations.NotNull


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_login
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btnLogin -> {
                viewModel.loginLiveData.value!!.email = binding.edtEmail.text.toString()
                viewModel.loginLiveData.value!!.password = binding.edtPasswd.text.toString()
                viewModel.loginLiveData.value!!.fcmToken = "fcm_token"
                viewModel.loginLiveData.value!!.deviceType = Constants.DEVICE_TYPE.toInt()
                when {
                    viewModel.loginLiveData.value!!.email.isNullOrEmpty() -> {
                        showError(baseActivity!!, getString(R.string.plz_enter_email_address))
                    }
                    (viewModel.loginLiveData.value!!.email!!.trim()).isNotEmpty() && !viewModel.loginLiveData.value!!.email!!.trim()
                        .isValidEmail() -> {
                        showError(baseActivity!!, getString(R.string.plz_enter_valid_email_address))
                    }
                    viewModel.loginLiveData.value!!.password.isNullOrEmpty() -> {
                        showError(baseActivity!!, getString(R.string.plz_enter_password))
                    }
                    /* viewModel.loginLiveData.value!!.password!!.length < 8 -> {
                         showError(baseActivity!!,getString(R.string.validPass))
                     }*/
                    else -> {
                        viewModel.hitLogin()
                    }
                }


            }
            R.id.txtForgotPassword -> {
                findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        }
    }


    private fun initUi() {
        binding.listener = this
        binding.viewModel = viewModel
        removeFlag()

        viewModel.loginLiveData.value?.deviceType = Constants.DEVICE_TYPE.toInt()
        viewModel.loginLiveData.value?.fcmToken = viewModel.getDeviceToken()
        if (viewModel.getDeviceToken() == "") {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                viewModel.saveDeviceToken(task.result?.toString())
                viewModel.loginLiveData.value?.fcmToken = task.result?.toString()
            }
        }
        binding.imageViewPasswordToggle.tag = R.drawable.ic_eye_close
        binding.imageViewPasswordToggle.showHidePassword(binding.edtPasswd)
        binding.edtEmail.onTextWritten()
        binding.edtPasswd.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) {
                setTextViewDrawableColor(binding.edtPasswd, R.color._B2D05A)
                binding.passwordLL.setBackgroundResource(R.drawable.background_stoke_highlight_edittext)
            } else {
                setTextViewDrawableColor(binding.edtPasswd, R.color._8F8F8F)
                binding.passwordLL.setBackgroundResource(R.drawable.background_stroke_edittext)
            }
        }

    }

    private fun setTextViewDrawableColor(
        @NotNull textView: TextView,
        @ColorRes color: Int
    ) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(baseActivity!!, color),
                        PorterDuff.Mode.SRC_IN
                    )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.edtEmail.setText("")
        binding.edtPasswd.setText("")
        baseActivity!!.getFirebaseToken()
    }

    override fun observeViewModel() {
        viewModel.loginResponseLiveData.observe(
            viewLifecycleOwner
        ) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!, it.data?.message!!)
                        Log.e("catch_exception_token", "save token: ${it.data.data?.authToken}")
                        viewModel.saveToken(it.data.data?.authToken)
                        viewModel.saveId(it.data.data?.id.toString())
                        viewModel.saveUser(it.data.data)
                        baseActivity!!.goToMainActivity()
                    }
                    is DataResult.Failure -> {
                        handleFailure(it.message, it.exception, it.errorCode)
                    }

                    else -> {}
                }
            }

        }

    }

    override fun initViewBinding() {

    }
}