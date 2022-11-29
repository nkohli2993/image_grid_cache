package com.rolling.meadows.views.view_main.dynamicPages

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentContactUsBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.ApiConstants
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.utils.hideKeyboard
import com.rolling.meadows.view_model.StaticPagesViewModel
import com.rolling.meadows.views.view_main.MainActivity
import com.rolling.meadows.views.view_main.dynamicPages.adapter.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>(), View.OnFocusChangeListener,
    BaseAdapter.OnItemClick {

    override fun getLayoutRes() = R.layout.fragment_contact_us
    private val viewModel: StaticPagesViewModel by viewModels()
    private var allCategories = arrayListOf(
        "Feedback", "Suggestion"
    )

    private fun initUi() {
        binding.listener = this
        binding.viewModel = viewModel
        if (baseActivity is MainActivity) {
            (baseActivity as MainActivity).setToolbar(true)
            (baseActivity as MainActivity).setTitle(getString(R.string.contact_us))
            //showCustomUI()
        }
//        binding.nameET.onTextWritten(binding.viewName)
        binding.emailET.onTextWritten(binding.viewEmail)
        binding.nameET.onFocusChangeListener = this
        binding.emailET.onFocusChangeListener = this
        binding.messageET.onFocusChangeListener = this

        binding.nameET.text = viewModel.getUser()?.fullName
        binding.emailET.setText(viewModel.getUser()?.email)
        viewModel.contactUsLiveData.value?.name = binding.nameET.text.toString()
        viewModel.contactUsLiveData.value?.email = binding.emailET.text.toString()
        val adapterCategory = CategoryAdapter(
            allCategories,
            baseActivity!!
        )
        binding.categoryRV.adapter = adapterCategory
        adapterCategory.setOnItemClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.backIV -> {
                findNavController().popBackStack()
            }
            R.id.selectCategoryCL, R.id.selectCategoryET -> {
                openSpinnerCategory()
            }
            R.id.submitBT -> {
                viewModel.contactUsLiveData.value?.email = binding.emailET.text.toString().trim()
                if (viewModel.contactUsLiveData.value?.email.isNullOrEmpty()) {
                    binding.emailET.setText("")
                    binding.emailET.error =
                        getString(R.string.plz_enter_email_address)
                    binding.emailET.requestFocus()

                } else if (!viewModel.contactUsLiveData.value?.email!!.isValidEmail()) {
                    binding.emailET.error =
                        getString(R.string.plz_enter_valid_email_address)
                    binding.emailET.requestFocus()

                } else if (viewModel.contactUsLiveData.value?.category.isNullOrEmpty()) {
                    showError(baseActivity!!, getString(R.string.plz_select_category))

                } else {
                    viewModel.onClickContactUs()
                }
            }
        }
    }

    private fun openSpinnerCategory() {
        if (binding.categoryRV.visibility == View.VISIBLE) {
            binding.categoryRV.visibleView(false)
            binding.imageSpinner.rotation = 0f
        } else {
            binding.categoryRV.visibleView(true)
            binding.imageSpinner.rotation = 180f
        }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        binding.nameET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.emailET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.messageET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.viewName.visibleView(false)
        binding.viewEmail.visibleView(false)
        when (p0?.id) {
            R.id.nameET -> {
                binding.viewName.visibleView(true)
                binding.nameET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.emailET -> {
                binding.viewEmail.visibleView(true)
                binding.emailET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.messageET -> {
                binding.messageET.setBackgroundResource(R.drawable.background_black_stroke)
            }
        }
    }


    override fun observeViewModel() {
        viewModel.contactUsResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showInfo(baseActivity!!, result.data?.message!!)
                        if (result.data.status == ApiConstants.SUCCESS) {
                            findNavController().navigate(R.id.homeFragment)
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
        initUi()
    }

    override fun onItemClick(vararg items: Any) {
        viewModel.contactUsLiveData.value?.category = ((items[0] as Int) + 1).toString()
        openSpinnerCategory()
        binding.selectCategoryET.setText(allCategories[items[0] as Int])
    }

    override fun onPause() {
        super.onPause()
        baseActivity!!.hideKeyboard()
    }
}