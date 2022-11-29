package com.rolling.meadows.views.authentication.forgot_password

import android.os.Bundle
import android.view.View
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
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>(),
    View.OnFocusChangeListener {
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
        binding.passwordET.onTextWritten(binding.viewPassword)
        binding.confirmPasswordET.onTextWritten(binding.viewConfirmPassword)
        binding.reservedTV.reservedWithVersion(baseActivity!!)
        binding.passwordToggle.tag = R.drawable.ic_hide
        binding.passwordToggle.showHidePassword(binding.passwordET)
        binding.confirmToggle.tag = R.drawable.ic_hide
        binding.confirmToggle.showHidePassword(binding.confirmPasswordET)

        binding.passwordET.onFocusChangeListener = this
        binding.confirmPasswordET.onFocusChangeListener = this
        when (type) {
            Constants.RESET_PASSWORD -> {
                passwordTV.text = getString(R.string.reset_password)
                continueBT.text = getString(R.string.reset)
            }
            else -> {
                passwordTV.text = getString(R.string.change_password)
                continueBT.text = getString(R.string.save_changes)
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.backIV ->{
                setBackPress()
            }
            R.id.continueBT -> {
                when (type) {
                    Constants.RESET_PASSWORD -> {
                        viewModel.resetPasswordLiveData.value.let {
                            it!!.userId = viewModel.getSaveUser()?.id.toString()
                            it.resetPasswordOtp =
                                viewModel.getSaveUser()?.phoneVerificationOtp.toString()
                            it.newPassword = binding.passwordET.text.toString().trim()
                            it.confirmNewPassword = binding.confirmPasswordET.text.toString().trim()
                            when {
                                it.newPassword.isNullOrEmpty() -> {
                                    binding.passwordET.error =
                                        getString(R.string.plz_enter_new_password)
                                    binding.passwordET.requestFocus()
                                }
                                it.confirmNewPassword.isNullOrEmpty() -> {
                                    binding.confirmPasswordET.error =
                                        getString(R.string.plz_enter_confirm_password)
                                    binding.confirmPasswordET.requestFocus()
                                }
                                it.newPassword.toString() != it.confirmNewPassword.toString() -> {
                                    binding.confirmPasswordET.error =
                                        getString(R.string.new_password_doesnot_matched_with_old_password)
                                    binding.confirmPasswordET.requestFocus()
                                }
                                else -> {
                                    viewModel.onClickResetPassword()
                                }
                            }
                        }
                    }
                    else -> {
                        viewModel.changePasswordLiveData.value?.let {
                            it.newPassword = binding.passwordET.text.toString().trim()
                            it.confirmNewPassword = binding.confirmPasswordET.text.toString().trim()
                            when {
                                it.newPassword.isNullOrEmpty() -> {
                                    binding.passwordET.error =
                                        getString(R.string.plz_enter_new_password)
                                    binding.passwordET.requestFocus()
                                }
                                it.confirmNewPassword.isNullOrEmpty() -> {
                                    binding.confirmPasswordET.error =
                                        getString(R.string.plz_enter_confirm_password)
                                    binding.confirmPasswordET.requestFocus()
                                }
                                it.newPassword.toString() != it.confirmNewPassword.toString() -> {
                                    binding.confirmPasswordET.error =
                                        getString(R.string.new_password_doesnot_matched_with_old_password)
                                    binding.confirmPasswordET.requestFocus()
                                }
                                else -> {
                                    viewModel.onClickChangePassword()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setBackPress() {
        when (type) {
            Constants.RESET_PASSWORD -> {
                findNavController().navigate(R.id.action_login)
            }
            else -> {
                baseActivity!!.onBackPressed()
            }
        }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        binding.passwordET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.confirmPasswordET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.viewPassword.visibleView(false)
        binding.viewConfirmPassword.visibleView(false)
        when (p0?.id) {
            R.id.passwordET -> {
                binding.viewPassword.visibleView(true)
                binding.passwordET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.confirmPasswordET -> {
                binding.viewConfirmPassword.visibleView(true)
                binding.confirmPasswordET.setBackgroundResource(R.drawable.background_black_stroke)
            }
        }
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