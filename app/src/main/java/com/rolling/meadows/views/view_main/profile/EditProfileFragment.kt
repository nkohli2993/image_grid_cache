package com.rolling.meadows.views.view_main.profile

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentEditProfileBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.extensions.*
import com.rolling.meadows.utils.observe
import com.rolling.meadows.view_model.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(), View.OnFocusChangeListener {

    override fun getLayoutRes() = R.layout.fragment_edit_profile
    private val viewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        removeFlag()
        binding.viewModel = viewModel
        binding.firstNameET.onTextWritten(binding.viewfullName)
        binding.lastNameET.onTextWritten(binding.viewlastName)
        binding.emailET.onTextWritten(binding.viewEmail)

        binding.lastNameET.onFocusChangeListener = this
        binding.firstNameET.onFocusChangeListener = this
        binding.emailET.onFocusChangeListener = this

    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        binding.firstNameET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.lastNameET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.emailET.setBackgroundResource(R.drawable.edittext_stroke)
        binding.viewfullName.visibleView(false)
        binding.viewlastName.visibleView(false)
        binding.viewEmail.visibleView(false)
        when (p0?.id) {

            R.id.firstNameET -> {
                binding.viewfullName.visibleView(true)
                binding.firstNameET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.lastNameET -> {
                binding.viewlastName.visibleView(true)
                binding.lastNameET.setBackgroundResource(R.drawable.background_black_stroke)
            }
            R.id.emailET -> {
                binding.viewEmail.visibleView(true)
                binding.emailET.setBackgroundResource(R.drawable.background_black_stroke)
            }
        }
    }

    override fun observeViewModel() {
        observe(baseActivity!!.selectedFile, ::handleSelectedImage)
        viewModel.profileResponseLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        showSuccess(baseActivity!!, result.data?.message!!)
                        viewModel.saveUser(result.data.data)
                        findNavController().popBackStack()
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
        binding.listener = this
        binding.viewModel = viewModel
        binding.imgProfile.loadCircleImage(
            viewModel.getSavedUser()?.profileImage,
            R.drawable.dummy_placeholder
        )
        binding.firstNameET.setText(viewModel.getSavedUser()?.firstName)
        binding.lastNameET.setText(viewModel.getSavedUser()?.lastName)
        binding.emailET.setText(viewModel.getSavedUser()?.email)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.backIV -> {
                findNavController().popBackStack()
            }
            R.id.imageFrame -> {
                if (!baseActivity!!.checkPermission()) {
                    baseActivity!!.requestPermission()
                } else {
                    baseActivity!!.openImagePicker()
                }
            }
            R.id.editBT -> {
                viewModel.userProfileData.value?.firstName = binding.firstNameET.text.toString()
                viewModel.userProfileData.value?.lastName = binding.lastNameET.text.toString()
                viewModel.userProfileData.value?.email = binding.emailET.text.toString().trim()
                if (viewModel.userProfileData.value?.firstName.toString().trim().isEmpty()) {
                    binding.firstNameET.setText("")
                    binding.firstNameET.error =
                        getString(R.string.plz_enter_full_name)
                    binding.firstNameET.requestFocus()

                } else if (viewModel.userProfileData.value?.lastName.toString().trim().isEmpty()) {
                    binding.lastNameET.setText("")
                    binding.lastNameET.error =
                        getString(R.string.plz_enter_last_name)
                    binding.lastNameET.requestFocus()

                } else if (!viewModel.userProfileData.value?.email.toString().trim()
                        .isEmpty() && !viewModel.userProfileData.value?.email!!.isValidEmail()
                ) {
                    binding.emailET.setText("")
                    binding.emailET.error =
                        getString(R.string.plz_enter_valid_email_address)
                    binding.emailET.requestFocus()

                } else {
                    viewModel.onClickUpdateProfile()
                }
            }
        }
    }

    private fun handleSelectedImage(file: File?) {
        if (file != null && file.exists()) {
            viewModel.userProfileData.value!!.profileFile = file.toString()
            binding.imgProfile.loadImageCentreCrop(R.drawable.ic_image_placeholder, file)
            binding.imgProfile.scaleType = ImageView.ScaleType.FIT_XY
        }
    }
}