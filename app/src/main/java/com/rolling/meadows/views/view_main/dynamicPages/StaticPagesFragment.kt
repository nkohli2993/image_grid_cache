package com.rolling.meadows.views.view_main.dynamicPages

import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentDynamicPageBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.observeEvent
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.showError
import com.rolling.meadows.utils.extensions.visibleView
import com.rolling.meadows.view_model.StaticPagesViewModel
import com.rolling.meadows.views.view_main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StaticPagesFragment : BaseFragment<FragmentDynamicPageBinding>() {
    override fun getLayoutRes() = R.layout.fragment_dynamic_page
    private val staticPagesViewModel: StaticPagesViewModel by viewModels()

    private fun init() {
        binding.toolbar.visibleView(false)
        //showCustomUI()
        when (arguments?.getString("page_from")) {
            "sign_up" -> {
                binding.toolbar.visibleView(true)
            }
        }
        when (arguments?.getString("type")) {
            Constants.ABOUT_US -> {
                // call for about us text
                staticPagesViewModel.type.value = Constants.PAGE_TYPE_ABOUT_US
            }
            Constants.PRIVACY -> {
                // call for privacy us text
                staticPagesViewModel.type.value = Constants.PAGE_TYPE_PRIVACY_POLICY
            }
            Constants.TERMSANDCONTION -> {
                // call for terms and condition us text
                staticPagesViewModel.type.value = Constants.PAGE_TYPE_TERMS_AND_CONDITION
            }
        }
        staticPagesViewModel.hitStaticPageApi()
        binding.ivBack.setOnClickListener {
           findNavController().popBackStack()
        }
    }

    override fun observeViewModel() {
        staticPagesViewModel.staticPageResponseLiveData.observeEvent(this) { result ->
            when (result) {
                is DataResult.Failure -> {
                    hideLoading()
                    showError(baseActivity!!, result.message.toString())
                }
                is DataResult.Loading -> {
                    showLoading("")
                }
                is DataResult.Success -> {
                    hideLoading()
                    binding.tvDescription.text =
                        HtmlCompat.fromHtml(result.data?.data?.description ?: "", 0)
                }
            }
        }


    }

    override fun initViewBinding() {
        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        changeStatusBarIconColor(true)
/*
        if (baseActivity is MainActivity) {
*/
/*
            (baseActivity as MainActivity).setToolbar(true)
*//*

            when (arguments?.getString("type")) {
                Constants.ABOUT_US -> {
                    (baseActivity as MainActivity).setTitle(getString(R.string.about_us))
                }
                Constants.PRIVACY -> {
                    (baseActivity as MainActivity).setTitle(getString(R.string.privacy_policy))
                }
                Constants.TERMSANDCONTION -> {
                    (baseActivity as MainActivity).setTitle(getString(R.string.terms_a_conditions))
                }
            }
        }
*/
        init()
    }

}