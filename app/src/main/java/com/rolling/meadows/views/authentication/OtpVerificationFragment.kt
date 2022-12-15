package com.rolling.meadows.views.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentOtpVerificationBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.ApiConstants
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.view_model.PhoneVerificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_otp_verification.*

@AndroidEntryPoint
class OtpVerificationFragment : BaseFragment<FragmentOtpVerificationBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_otp_verification
    private val viewModel: PhoneVerificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun initUi() {
        binding.listener = this
        removeFlag()

        binding.firstET.onOTPTextWritten()
        binding.secondET.onOTPTextWritten()
        binding.threeET.onOTPTextWritten()
        binding.fourET.onOTPTextWritten()

        binding.firstET.otpHelper()
        binding.secondET.otpHelper()
        binding.threeET.otpHelper()
        binding.fourET.otpHelper()

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btnContinue -> {
                val otp = binding.firstET.text.toString().trim()
                    .plus(binding.secondET.text.toString().trim())
                    .plus(binding.threeET.text.toString().trim())
                    .plus(binding.fourET.text.toString().trim())
                viewModel.otpVerificationLiveData.value?.otp = otp
                viewModel.otpVerificationLiveData.value?.user_id =
                    viewModel.getUser()?.id.toString()

                when {
                    otp.isEmpty() -> {
                        showError(baseActivity!!, getString(R.string.plz_enter_otp))
                    }
                    otp.length < 4 -> {
                        showError(baseActivity!!, getString(R.string.plz_enter_valid_otp))
                    }
                    else -> {
                        viewModel.onClickOtpVerify()
                    }
                }
            }
            R.id.backIV -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun setView() {
        binding.resendOtpTV.setSpanString(
            baseActivity!!.getString(R.string.dont_have_account_resend_otp),
            startPos = 19,
            isBold = true,
            onClick = {
                viewModel.otpVerificationLiveData.value?.user_id =
                    viewModel.getUser()?.id.toString()
                viewModel.onClickResetOtpVerify()
            })
    }

    override fun observeViewModel() {
        observerVerifiedOTP()
        observerResendOtp()
    }

    private fun observerResendOtp() {
        viewModel.resendOtpVerificationLiveDataResponse.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!,result.data?.message!!.replace("otp", "OTP").replace("Otp", "OTP"))
/*
                        showSuccess(
                            baseActivity!!,
                            result.data?.message!!.replace("otp", "OTP").replace("Otp", "OTP").plus(". New Otp is  ${result.data?.data?.emailVerificationOtp}")
                        )
*/
                    }
                    is DataResult.Failure -> {
                        handleFailure(result.message, result.exception, result.errorCode)
                    }

                    else -> {}
                }
            }

        }
    }

    private fun observerVerifiedOTP() {
        viewModel.otpVerificationResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!,
                            result.data?.message!!.replace("otp", "OTP").replace("Otp", "OTP")
                        )
                        if (result.data.status == ApiConstants.SUCCESS) {
                            val bundle = Bundle()
                            viewModel.saveToken(result.data.data?.authToken)
                            viewModel.saveUser(result.data.data)
                            bundle.putString("type", Constants.RESET_PASSWORD)
                            bundle.putString(
                                "otp",
                                viewModel.otpVerificationLiveData.value?.otp
                            )
                            findNavController().navigate(R.id.action_reset_otp, bundle)
                        }
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
