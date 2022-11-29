package com.rolling.meadows.views.authentication.register

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentRegisterBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.utils.observe
import com.rolling.meadows.view_model.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import java.io.File

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(), View.OnFocusChangeListener {
    override fun getLayoutRes(): Int = R.layout.fragment_register
    private val viewModel: RegisterViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
    }

    private fun initUi() {
       removeFlag()
        baseActivity!!.selectedFile.value = null
        binding.emailET.onTextWritten(binding.viewEmail)
        binding.passwordET.onTextWritten(binding.viewPassword)
        binding.passwordToggle.tag = R.drawable.ic_hide
        binding.passwordToggle.showHidePassword(binding.passwordET)
        binding.confirmPasswordToggle.tag = R.drawable.ic_hide
        binding.confirmPasswordToggle.showHidePassword(binding.confirmPasswordET)
        binding.firstNameET.onTextWritten(binding.viewfullName)
        binding.lastNameET.onTextWritten(binding.viewlastName)
        binding.phoneET.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                binding.phoneLL.setBackgroundResource(R.drawable.background_black_stroke)
                binding.viewmobile.visibleView(true)
            } else {
                binding.phoneLL.setBackgroundResource(R.drawable.edittext_stroke)
                binding.viewmobile.visibleView(false)
            }

        }

        binding.emailET.onFocusChangeListener = this
        binding.passwordET.onFocusChangeListener = this
        binding.lastNameET.onFocusChangeListener = this
        binding.firstNameET.onFocusChangeListener = this
        binding.phoneET.onFocusChangeListener = this
        binding.codePicker.setCountryForPhoneCode(+243)
        binding.loginTV.setSpanString(
            baseActivity!!.getString(R.string.already_have_an_account_sign_in),
            startPos = 25,
            isBold = true,

            onClick = {
                findNavController().popBackStack()
            })
        binding.termsTV.setSpanString(
            baseActivity!!.getString(R.string.agree_terms_privacy_policy),
            startPos = 25,
            endPos = 44,
            isBold = true,
            isUnderLine = false,
            onClick = {

                val bundle = Bundle()
                bundle.putString("type", Constants.TERMSANDCONTION)
                bundle.putString("page_from", Constants.SIGNUP)
                findNavController().navigate(R.id.staticPagesFragment, bundle)
            })

        binding.backIV.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.viewModel = viewModel
        codePickerHandling()
    }

    private fun codePickerHandling() {
        val typeFace = ResourcesCompat.getFont(requireContext(), R.font.poppins_regular)
        binding.codePicker.setTypeFace(typeFace)
        viewModel.registrationLiveData.value?.deviceType = 1
        viewModel.registrationLiveData.value?.role = "1"
        viewModel.registrationLiveData.value?.fcmToken = viewModel.getDeviceToken()
        if (viewModel.registrationLiveData.value?.phoneCode.isNullOrEmpty()) {
            viewModel.registrationLiveData.value?.phoneCode =
                binding.codePicker.selectedCountryCodeWithPlus
            viewModel.registrationLiveData.value?.isoCode =
                binding.codePicker.selectedCountryNameCode
        } else {
            with(binding) {
                codePicker.setCountryForPhoneCode(
                    viewModel?.registrationLiveData?.value?.phoneCode!!.toInt()
                )
                codePicker.setCountryForNameCode(
                    viewModel?.registrationLiveData?.value?.isoCode!!.toString()
                )
            }
        }

        binding.codePicker.setOnCountryChangeListener {
            viewModel.registrationLiveData.value?.phoneCode =
                binding.codePicker.selectedCountryCodeWithPlus
            viewModel.registrationLiveData.value?.isoCode =
                binding.codePicker.defaultCountryNameCode
        }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        binding.firstNameET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.lastNameET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.emailET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.phoneLL.setBackgroundResource(R.drawable.edittext_stroke)
        binding.passwordET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.viewfullName.visibleView(false)
        binding.viewlastName.visibleView(false)
        binding.viewEmail.visibleView(false)
        binding.viewmobile.visibleView(false)
        binding.viewPassword.visibleView(false)
        when (p0?.id) {
            R.id.emailET -> {
                binding.viewEmail.visibleView(true)
                binding.emailET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.passwordET -> {
                binding.viewPassword.visibleView(true)
                binding.passwordET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.firstNameET -> {
                binding.viewfullName.visibleView(true)
                binding.firstNameET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.lastNameET -> {
                binding.viewlastName.visibleView(true)
                binding.lastNameET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.phoneET -> {
                binding.viewmobile.visibleView(true)
                binding.phoneLL.setBackgroundResource(R.drawable.background_black_stroke)
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.imageFrame -> {
                if (!baseActivity!!.checkPermission()) {
                    baseActivity!!.requestPermission()
                } else {
                    baseActivity!!.openImagePicker()
                }
            }
        }
    }

    private fun handleSelectedImage(file: File?) {
        if (file != null && file.exists()) {
            binding.imgProfile.visibility = View.GONE
            binding.selectedImageIV.visibility = View.VISIBLE
            viewModel.registrationLiveData.value!!.profileFile = file.toString()
            binding.selectedImageIV.loadImageCentreCrop(R.drawable.ic_image_placeholder, file)
            binding.selectedImageIV.scaleType = ImageView.ScaleType.FIT_XY
        }
    }

    override fun observeViewModel() {
        observe(baseActivity!!.selectedFile, ::handleSelectedImage)
        viewModel.registerResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!, result.data?.message!!)
//                        showSuccess(baseActivity!!,"YOUR ROVES VERIFICATION CODE IS ${result.data?.data?.phoneVerificationOtp}")
                        viewModel.saveToken(result.data.data?.auth_token)
                        viewModel.saveId(result.data?.data?.id.toString())
                        viewModel.saveUser(result.data.data)
                        findNavController().navigate(
                            RegisterFragmentDirections.actionSendOtp(
                                "registration_account",
                                viewModel.registrationLiveData.value!!.phoneCode,
                                viewModel.registrationLiveData.value!!.phoneCode.plus(" ${viewModel.registrationLiveData.value!!.phoneNumber}"),
                                ((result.data?.data?.phoneVerificationOtp ?: "") as Int
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