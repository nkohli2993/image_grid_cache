package com.rolling.meadows.views.authentication.forgot_password

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentForgotPasswordBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.view_model.ForgotPasswordViewModel
import com.rolling.meadows.views.authentication.register.RegisterFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_forgot_password.*

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



    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.continueBT -> {

            }
            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
        }
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
                        findNavController().navigate(
                            RegisterFragmentDirections.actionSendOtp(
                                "forgot_password",
                                viewModel.forgotPasswordLiveData.value!!.phoneCode,
                                viewModel.forgotPasswordLiveData.value!!.phoneCode.plus(" ${viewModel.forgotPasswordLiveData.value!!.phoneNumber}"),
                                ((result.data.data?.phoneVerificationOtp ?: "") as Int
                                    ?: 0).toString()
                            )
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

    override fun initViewBinding() {
        initUi()
    }
}