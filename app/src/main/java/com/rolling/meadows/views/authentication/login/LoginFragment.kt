package com.rolling.meadows.views.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateLayoutParams
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
import com.rolling.meadows.views.authentication.register.RegisterFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(){
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
        when(v?.id){
            R.id.btnLogin->{
                baseActivity!!.goToMainActivity()
            }
        }
    }


    private fun initUi() {

        /* viewModel.loginLiveData.value?.deviceType = Constants.DEVICE_TYPE
         viewModel.loginLiveData.value?.fcmToken = viewModel.getDeviceToken()
         if(viewModel.getDeviceToken() == ""){
             FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
                 if (!task.isSuccessful) {
                     return@addOnCompleteListener
                 }
                 viewModel.saveDeviceToken(task.result?.toString())
                 viewModel.loginLiveData.value?.fcmToken = task.result?.toString()
             }
         }
         viewModel.loginLiveData.value?.role = Constants.CUSTOMER_ROLE
         viewModel.loginLiveData.value?.phoneCode = binding.codePicker.selectedCountryCodeWithPlus
         viewModel.loginLiveData.value?.isoCode = binding.codePicker.defaultCountryNameCode
         binding.codePicker.setOnCountryChangeListener {
             viewModel.loginLiveData.value?.phoneCode =
                 binding.codePicker.selectedCountryCodeWithPlus
             viewModel.loginLiveData.value?.isoCode = binding.codePicker.defaultCountryNameCode
         }*/

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
                            Prefs.save(Constants.RIDE_ID,"login")
                            baseActivity!!.goToMainActivity()
                        } else if (it.data?.message == "Please verify your phone number.") {
                            showSuccess(baseActivity!!, it.data.message!!)
                            viewModel.saveToken(it.data.data?.auth_token)
                            viewModel.saveUser(it.data.data)
                            findNavController().navigate(
                                RegisterFragmentDirections.actionSendOtp(
                                    "registration_account",
                                    viewModel.loginLiveData.value!!.phoneCode,
                                    viewModel.loginLiveData.value!!.phoneCode.plus(" ${viewModel.loginLiveData.value!!.phone_number}"),
                                    ((it.data.data?.phoneVerificationOtp ?: "") as Int
                                        ?: 0).toString()
                                )
                            )
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