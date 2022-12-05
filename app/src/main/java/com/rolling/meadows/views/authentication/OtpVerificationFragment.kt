package com.rolling.meadows.views.authentication

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpVerificationFragment : BaseFragment<FragmentOtpVerificationBinding>()
  {
    override fun getLayoutRes(): Int = R.layout.fragment_otp_verification
    private val viewModel: PhoneVerificationViewModel by viewModels()
    private var type: String? = null
    private var dialog: Dialog? = null
    private var otp: String? = null
    private var phoneNumber: String? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
              /*  viewModel.otpVerificationLiveData.value?.otp = otp
                viewModel.otpVerificationLiveData.value?.user_id =
                    viewModel.getUser()?.id.toString()*/

                when {
                    otp.isEmpty() -> {
                        showError(baseActivity!!, getString(R.string.plz_enter_otp))
                    }
                    otp.length < 4 -> {
                        showError(baseActivity!!, getString(R.string.plz_enter_valid_otp))
                    }
                    else -> {
                        findNavController().navigate(R.id.action_reset_otp)
/*
                        when (type) {
                            "registration_account" -> {
                                viewModel.onClickOtpVerify()
                            }
                            else -> {
                                viewModel.onClickResetOtpVerify()
                            }
                        }
*/

                    }
                }
            }
            R.id.backIV -> {
                when (type) {
                    "registration_account" -> {
                       findNavController().navigate(R.id.action_introFragment_to_loginFragment)
                    }
                    else -> {
                        baseActivity!!.onBackPressed()
                    }
                }


            }
        }
    }

    private fun setView() {
        binding.resendOtpTV.setSpanString(
            baseActivity!!.getString(R.string.dont_have_account_resend_otp),
            startPos = 19,
            isBold = true,
            onClick = {
                showInfo(baseActivity!!, getString(R.string.your_new_one_time_will_send_to_you))
               /* viewModel.resendOtpVerificationLiveData.value?.user_id =
                    viewModel.getUser()?.id.toString()
                viewModel.hitResendOtpVerification()*/
            })
    }

    @SuppressLint("SetTextI18n")
    private fun showAccountVerificationDialog() {
        dialog = Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_account_verification)
        dialog?.setCancelable(false)
        val welcomeTxt = dialog?.findViewById(R.id.welcomeTxt) as AppCompatTextView
        welcomeTxt.text =
            "${getString(R.string.hey_)} ${viewModel.getUser()?.fullName} ${getString(R.string.hey_account_verifed_text)}"
        lifecycleScope.launch {
            delay(2000)
            baseActivity!!.goToMainActivity()
        }
        dialog?.show()
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
//                        showSuccess(baseActivity!!,"YOUR ROVES VERIFICATION CODE IS ${result.data?.data?.phoneVerificationOtp}")
                         showSuccess(
                             baseActivity!!,
                             result.data?.message!!.replace("otp", "OTP").replace("Otp", "OTP")
                         )
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
                        showToast(result.data?.message!!.replace("otp", "OTP").replace("Otp", "OTP"))
                        if (result.data.status == ApiConstants.SUCCESS) {
                            when (type) {
                                "registration_account" -> {
                                    viewModel.saveToken(result.data.data?.auth_token)
                                    viewModel.saveUser(result.data.data)
                                    showAccountVerificationDialog()
                                }
                                else -> {
                                    val bundle = Bundle()
                                    viewModel.saveToken(result.data.data?.auth_token)
                                    viewModel.saveUser(result.data.data)
                                    bundle.putString("type", Constants.RESET_PASSWORD)
                                    bundle.putString(
                                        "otp",
                                        viewModel.otpVerificationLiveData.value?.otp
                                    )
                                    findNavController().navigate(R.id.action_reset_otp, bundle)
                                }
                            }
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
