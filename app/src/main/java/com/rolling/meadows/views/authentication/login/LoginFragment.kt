package com.rolling.meadows.views.authentication.login

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
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
                when {
                    binding.edtEmail.text.isNullOrEmpty() -> {
                        binding.edtEmail.error =
                            getString(R.string.plz_enter_email_address)
                        binding.edtEmail.requestFocus()
                    }
                    (binding.edtEmail.text?.trim()
                        ?: "").isNotEmpty() && !isValidEmail() -> {
                        binding.edtEmail.error =
                            getString(R.string.plz_enter_valid_email_address)
                        binding.edtEmail.requestFocus()
                    }
                    binding.edtPasswd.text.isNullOrEmpty() -> {
                        binding.edtPasswd.error =
                            getString(R.string.plz_enter_password)
                        binding.edtPasswd.requestFocus()
                    }
                    else -> {
                        baseActivity!!.goToMainActivity()
                    }
                }
            }
            R.id.txtForgotPassword -> {
                findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        }
    }

    private fun isValidEmail(): Boolean {
      return  binding.edtEmail.text.toString().isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString()).matches()
    }


    private fun initUi() {
        binding.listener = this
        removeFlag()
        viewModel.loginLiveData.value?.deviceType = Constants.DEVICE_TYPE
        viewModel.loginLiveData.value?.fcmToken = viewModel.getDeviceToken()
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
        //  baseActivity!!.getFirebaseToken()
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
                        if (it.data?.message == "User logged in successfully") {
                            showSuccess(baseActivity!!, it.data.message!!)
                            viewModel.saveToken(it.data.data?.auth_token)
                            viewModel.saveId(it.data.data?.id.toString())
                            viewModel.saveUser(it.data.data)
                            Prefs.save(Constants.RIDE_ID, "login")
                            baseActivity!!.goToMainActivity()
                        } else if (it.data?.message == "Please verify your phone number.") {
                            showSuccess(baseActivity!!, it.data.message!!)
                            viewModel.saveToken(it.data.data?.auth_token)
                            viewModel.saveUser(it.data.data)
                            //send email and otp
                            findNavController().navigate(R.id.action_send_otp)
                        }
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