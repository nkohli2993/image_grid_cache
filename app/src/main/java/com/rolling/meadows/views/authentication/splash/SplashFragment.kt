package com.rolling.meadows.views.authentication.splash

import android.os.Bundle
import android.provider.Settings
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.cache.Prefs
import com.rolling.meadows.data.authentication.LoginRequestModelData
import com.rolling.meadows.databinding.FragmentSplashBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.extensions.reservedWithVersion
import com.rolling.meadows.utils.extensions.showSuccess
import com.rolling.meadows.view_model.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_splash
    private val viewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCustomUI()
    }

    override fun onResume() {
        super.onResume()
        baseActivity!!.getFirebaseToken()
        val android_device_id =
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
        Log.e("catch_exception","unique:id: ${android_device_id}")
        lifecycleScope.launch {
            delay(3000)
            activity?.runOnUiThread {
                viewModel.hitLogin(LoginRequestModelData(com.rolling.meadows.utils.Constants.DEVICE_TYPE,viewModel.getDeviceToken(),android_device_id!!))
            }
        }
    }

    override fun observeViewModel() {
        viewModel.profileResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!, getString(R.string.welcome_back))
                        viewModel.saveId(result.data?.data?.id.toString())
                        viewModel.saveUser(result.data!!.data)
                        baseActivity!!.goToMainActivity()
                    }
                    is DataResult.Failure -> {
                        viewModel.saveToken(null)
                        // handleFailure(result.message, result.exception, result.errorCode)
                        baseActivity!!.goToInitialActivity()
                    }

                    else -> {}
                }
            }

        }
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
//                        showSuccess(baseActivity!!, it.data?.message!!)
                        Log.e("catch_exception_token", "save token: ${it.data?.data?.authToken}")
                        viewModel.saveToken(it.data?.data?.authToken)
                        viewModel.saveId(it.data?.data?.id.toString())
                        viewModel.saveUser(it.data?.data)
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