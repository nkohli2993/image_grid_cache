package com.rolling.meadows.views.authentication.forgot_password

import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentForgotPasswordBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.view_model.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_forgot_password
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.saveUser(null)
    }

    private fun initUi() {
        binding.listener = this
        binding.edtEmail.onTextWritten()
        removeFlag()
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.submitBT -> {
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
                    else -> {
                        findNavController().navigate(R.id.action_send_otp)
                    }
                }


            }
            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return binding.edtEmail.text.toString().isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString()).matches()
    }


    override fun observeViewModel() {
        viewModel.forgotPasswordResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showInfo(baseActivity!!, result.data?.message!!)
                        viewModel.saveUser(result.data.data)
                        findNavController().navigate(R.id.action_send_otp)
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
        initUi()
    }
}