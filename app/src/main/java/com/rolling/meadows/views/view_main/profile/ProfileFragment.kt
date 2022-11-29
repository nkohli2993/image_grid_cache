package com.rolling.meadows.views.view_main.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.databinding.FragmentProfileBinding
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.loadCircleImage
import com.rolling.meadows.utils.extensions.reservedWithVersion
import com.rolling.meadows.view_model.ProfileViewModel
import com.rolling.meadows.views.view_main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.popup_language.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(), View.OnClickListener {
    override fun getLayoutRes(): Int = R.layout.fragment_profile
    private var mainActivity: MainActivity? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
        if (baseActivity is MainActivity) {
            (baseActivity as MainActivity).setToolbar(false)
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.ivMenu -> {
                mainActivity?.openDrawer()
            }
            R.id.changePasswordCL -> {
                val bundle = Bundle()
                bundle.putString("type", Constants.CHANGE_PASSWORD)
                findNavController().navigate(R.id.action_change_password, bundle)
            }
            R.id.ivEdit -> {
                findNavController().navigate(R.id.action_edit_profile)
            }
            R.id.ridesCL -> {
                val bundle = Bundle()
                bundle.putString("page_from", Constants.PROFILE)
                findNavController().navigate(R.id.action_booking_request, bundle)
            }
            R.id.languageTV, R.id.dropdownIV -> {
                rotate(180f)
                showPopupReportPost(binding.describeTV)
            }
        }
    }

    private fun rotate(degree: Float) {
        val rotateAnim = RotateAnimation(
            0.0f, degree,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnim.duration = 0
        rotateAnim.fillAfter = true
        binding.dropdownIV.startAnimation(rotateAnim)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showPopupReportPost(
        view: AppCompatTextView
    ) {
        val popupView: View =
            LayoutInflater.from(baseActivity!!).inflate(
                R.layout.popup_language,
                null
            )
        val width: Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val height: Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        val englishTV: AppCompatTextView =
            popupView.findViewById(R.id.englistTV) as AppCompatTextView
        val frenchTV: AppCompatTextView =
            popupView.findViewById(R.id.frenchTV) as AppCompatTextView
        val swahiliiTV: AppCompatTextView =
            popupView.findViewById(R.id.swahiliiTV) as AppCompatTextView
        popupWindow.showAtLocation(
            popupView,
            Gravity.TOP or Gravity.LEFT,
            popupLocateView(view)?.left!!,
            popupLocateView(view)?.top!!
        )
        // show the dim background
        val container: View = if (popupWindow.background == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.contentView.parent as View
            } else {
                popupWindow.contentView
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.contentView.parent.parent as View
            } else {
                popupWindow.contentView.parent as View
            }
        }
        val context: Context = popupWindow.contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.1f
        p.horizontalMargin = 10.0f
        wm.updateViewLayout(container, p)
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
        englishTV.setOnClickListener {
            binding.languageTV.text = getString(R.string.english)
            popupWindow.dismiss()
        }
        frenchTV.setOnClickListener {
            binding.languageTV.text = getString(R.string.french)
            popupWindow.dismiss()
        }
        swahiliiTV.setOnClickListener {
            binding.languageTV.text = getString(R.string.swahili)
            popupWindow.dismiss()
        }

        popupWindow.setOnDismissListener {
            rotate(0f)
        }

    }

    private fun popupLocateView(v: View?): Rect? {
        val locInt = IntArray(2)
        if (v == null) return null
        try {
            v.getLocationOnScreen(locInt)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null
        }
        val location = Rect()
        location.left = locInt[0]
        location.top = locInt[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.listener = this
        binding.viewModel = viewModel
        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        changeStatusBarIconColor(true)
        if (baseActivity is MainActivity) {
            (baseActivity as MainActivity).setToolbar(false)
        }

        binding.reservedTV.reservedWithVersion(baseActivity!!)

    }

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        //show profile info
        //showCustomUI()
        binding.imageIV.loadCircleImage(
            viewModel.getSavedUser()?.profileImage,
            R.drawable.dummy_placeholder
        )
        binding.rideCountTV.text = viewModel.getSavedUser()?.totalRides.toString()
    }
}