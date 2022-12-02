package com.rolling.meadows.views.authentication.forgot_password

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentResetPasswordBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.view_model.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_reset_password.*

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_reset_password
    private var type: String? = null
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.listener = this
        type = arguments?.getString("type")
        removeFlag()
        binding.passwordET.onTextWritten()
        binding.confirmPasswordET.onTextWritten()

        binding.passwordET.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) {
                setTextViewDrawableColor(binding.passwordET,R.color._B2D05A)
                binding.passwordCL.setBackgroundResource(R.drawable.background_stoke_highlight_edittext)
            } else {
                setTextViewDrawableColor(binding.passwordET,R.color._8F8F8F)
                binding.passwordCL.setBackgroundResource(R.drawable.background_stroke_edittext)
            }
        }

        binding.confirmPasswordET.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) {
                setTextViewDrawableColor(binding.confirmPasswordET,R.color._B2D05A)
                binding.confirmPasswordCL.setBackgroundResource(R.drawable.background_stoke_highlight_edittext)
            } else {
                setTextViewDrawableColor(binding.confirmPasswordET,R.color._8F8F8F)
                binding.confirmPasswordCL.setBackgroundResource(R.drawable.background_stroke_edittext)
            }
        }

        binding.passwordToggle.tag = R.drawable.ic_eye_close
        binding.passwordToggle.showHidePassword(binding.passwordET)
        binding.confirmToggle.tag = R.drawable.ic_eye_close
        binding.confirmToggle.showHidePassword(binding.confirmPasswordET)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.backIV -> {
                setBackPress()
            }
            R.id.continueBT -> {
                when {
                    binding.passwordET.text.isNullOrEmpty() -> {
                        binding.passwordET.error =
                            getString(R.string.plz_enter_new_password)
                        binding.passwordET.requestFocus()
                    }
                    binding.confirmPasswordET.text.isNullOrEmpty() -> {
                        binding.confirmPasswordET.error =
                            getString(R.string.plz_enter_confirm_password)
                        binding.confirmPasswordET.requestFocus()
                    }
                    binding.passwordET.text.toString() != binding.confirmPasswordET.text.toString() -> {
                        binding.confirmPasswordET.error =
                            getString(R.string.new_password_doesnot_matched_with_old_password)
                        binding.confirmPasswordET.requestFocus()
                    }
                    else -> {
                        findNavController().navigate(R.id.action_introFragment_to_loginFragment)
//                        viewModel.onClickResetPassword()
                    }
                }
            }
        }
    }

    private fun setBackPress() {
        baseActivity!!.onBackPressed()
    }


    override fun observeViewModel() {
        viewModel.resetPasswordResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!, result.data?.message!!)
                        setBackPress()
                    }
                    is DataResult.Failure -> {
                        handleFailure(result.message, result.exception, result.errorCode)
                    }

                    else -> {}
                }
            }

        }
        viewModel.changePasswordResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!, result.data?.message!!)
                        viewModel.saveUser(null)
                        viewModel.saveToken(null)
                        baseActivity!!.goToInitialActivity()
                    }
                    is DataResult.Failure -> {
                        handleFailure(result.message, result.exception, result.errorCode)
                    }

                    else -> {}
                }
            }

        }

    }

    override fun initViewBinding() {

    }
}