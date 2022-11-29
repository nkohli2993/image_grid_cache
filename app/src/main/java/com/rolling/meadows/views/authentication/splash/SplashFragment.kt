package com.rolling.meadows.views.authentication.splash

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.cache.Prefs
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
        //showCustomUI()
        changeStatusBarColor(ContextCompat.getColor(baseActivity!!, R.color.colorPrimary))
        changeStatusBarIconColor(false)
    }

    override fun onResume() {
        super.onResume()
        Prefs.saveRideData("ride_data",null)
        lifecycleScope.launch {
            delay(3000)
            activity?.runOnUiThread {
                findNavController().navigate(R.id.action_introFragment_to_loginFragment)
/*
                if (viewModel.getToken().isNullOrEmpty()) {
                    findNavController().navigate(R.id.action_introFragment_to_loginFragment)
                } else {
                    viewModel.getProfile()
                }
*/
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

                }
            }

        }

    }

    override fun initViewBinding() {

    }

}