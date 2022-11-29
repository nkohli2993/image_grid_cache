package com.rolling.meadows.views.view_main

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationManager
import android.content.*
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.libraries.places.api.Places
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.cache.Prefs
import com.rolling.meadows.databinding.ActivityMainBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.showAlertDialog
import com.rolling.meadows.utils.extensions.showException
import com.rolling.meadows.utils.extensions.visibleView
import com.rolling.meadows.view_model.LogoutViewModel
import com.rolling.meadows.views.view_main.profile.ProfileFragment
import com.rolling.meadows.views.view_main.bookings.BookingRequestFragment
import com.rolling.meadows.views.view_main.dynamicPages.ContactUsFragment
import com.rolling.meadows.views.view_main.dynamicPages.StaticPagesFragment
import com.rolling.meadows.views.view_main.homePages.HomeFragment
import com.rolling.meadows.views.view_main.homePages.ViewTypeOpenViewModel
import com.rolling.meadows.views.view_main.payment.SelectPaymentFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity(), View.OnClickListener {
    var noDriverDialog: Dialog? = null
    private lateinit var binding: ActivityMainBinding
    private var dialog: Dialog? = null
    var navController: NavController? = null
    var preferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private val viewModel: LogoutViewModel by viewModels()
    private val viewModelClick: ViewTypeOpenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.home_nav_host_fragment)
        preferences =
            getSharedPreferences("page_navigation", Context.MODE_PRIVATE)
        editor = preferences!!.edit()

        Prefs.editor
        init()
        logoutObserveData()
        initPlacesAutoComplete()
    }

    private fun initPlacesAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_key))
        }
    }


    private fun init() {
        binding.listener = this

        if (intent != null && intent.hasExtra("isPush")) {
            intent.getBundleExtra("detail")?.let { checkAction(it) }
        }
    }

    override fun onStop() {
        Prefs.saveRideData("ride_data", null)
        super.onStop()
    }

    private fun checkAction(bundle: Bundle) {
        if (viewModel.getToken() != null || viewModel.getToken() != "") {
            if (bundle.getString("type") != null) {
                when (bundle.getString("type")!!) {
                    Constants.NotificationType.NOTIFICATION_TYPE_RIDE_BOOK.value.toString(),
                    Constants.NotificationType.NOTIFICATION_TYPE_RIDE_ACCEPTED.value.toString(),
                    Constants.NotificationType.NOTIFICATION_TYPE_RIDE_ONGOING.value.toString(),
                    Constants.NotificationType.NOTIFICATION_TYPE_RIDE_REJECTED.value.toString(),
                    Constants.NotificationType.NOTIFICATION_TYPE_RIDE_PICKUP.value.toString(),
                    Constants.NotificationType.NOTIFICATION_TYPE_RIDE_AS_REACHED.value.toString() -> {
                        navController?.navigate(R.id.homeFragment)
                    }
                    else -> {
                        when (bundle.getString("type")!!) {
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_CANCELED.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_COMPLETED.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_AUTO_CANCEL.value.toString() -> {
                                Prefs.save(Constants.RIDE_ID, "")
                            }
                        }
                        val bundleIntent = Bundle()
                        bundleIntent.putInt("id", bundle.getString("ride_id")!!.toInt())
                        navController?.navigate(R.id.action_booking_request_detail, bundleIntent)
                    }
                }

            } else {
                navController?.navigate(R.id.homeFragment)
            }
        } else {
            goToInitialActivity()
        }
        clearNotification()
    }

    fun setToolbar(value: Boolean) {
        if (binding != null) {
            toolbar.visibleView(value)
        }
    }

    fun setTitle(title: String) {
        if (binding != null) {
            titleTV.text = title
        }
        setDefaultColorIcons()
        showOpenFragmentHighLighted()
    }

    fun openDrawer() {
        setDefaultColorIcons()
        binding.logoutIV.visibility = View.VISIBLE
        showOpenFragmentHighLighted()
        binding.drawer.openDrawer(GravityCompat.END)
    }

    private fun showOpenFragmentHighLighted() {
        val navHostFragment = supportFragmentManager.primaryNavigationFragment as NavHostFragment?
        val fragmentManager: FragmentManager = navHostFragment!!.childFragmentManager
        binding.ivNotification.visibleView(false)
        when (fragmentManager.primaryNavigationFragment!!) {
            is HomeFragment -> {
                binding.ivNotification.visibleView(true)
                setSelectedTextColorIcon(
                    binding.homeIV,
                    R.drawable.ic_home_selected,
                    binding.homeTV,
                    ContextCompat.getColor(this, R.color.white)
                )
                binding.logoutIV.visibility = View.VISIBLE
            }
            is BookingRequestFragment -> {
                Prefs.saveRideData("ride_data", null)
                setSelectedTextColorIcon(
                    binding.rideIV,
                    R.drawable.ic_my_rides_selected,
                    binding.rideTV,
                    ContextCompat.getColor(this, R.color.white)
                )
            }
            is ProfileFragment -> {
                Prefs.saveRideData("ride_data", null)
                setSelectedTextColorIcon(
                    binding.profileIV,
                    R.drawable.ic_profile_selected,
                    binding.profileTV,
                    ContextCompat.getColor(this, R.color.white)
                )
            }
            is StaticPagesFragment -> {
                //add check for page type
                Prefs.saveRideData("ride_data", null)
                when (binding.titleTV.text) {
                    getString(R.string.about_us) -> {
                        setSelectedTextColorIcon(
                            binding.aboutIV,
                            R.drawable.ic_about_us_selected,
                            binding.aboutTV,
                            ContextCompat.getColor(this, R.color.white)
                        )

                    }
                    getString(R.string.privacy_policy) -> {
                        setSelectedTextColorIcon(
                            binding.privacyIV,
                            R.drawable.ic_privacy_selected,
                            binding.privacyTV,
                            ContextCompat.getColor(this, R.color.white)
                        )
                    }
                    else -> {
                        setSelectedTextColorIcon(
                            binding.termsIV,
                            R.drawable.ic_terms_selected,
                            binding.termsTV,
                            ContextCompat.getColor(this, R.color.white)
                        )
                    }
                }
            }
            is ContactUsFragment -> {
                Prefs.saveRideData("ride_data", null)
                setSelectedTextColorIcon(
                    binding.contactUsIV,
                    R.drawable.ic_contactus_selected,
                    binding.contactUsTV,
                    ContextCompat.getColor(this, R.color.white)
                )
            }
            is SelectPaymentFragment -> {
                Prefs.saveRideData("ride_data", null)
                setSelectedTextColorIcon(
                    binding.paymentIV,
                    R.drawable.ic_selected_payment,
                    binding.paymentTV,
                    ContextCompat.getColor(this, R.color.white)
                )
            }
        }
    }

    private fun setSelectedTextColorIcon(
        imageView: AppCompatImageView,
        image: Int,
        textView: AppCompatTextView,
        color: Int
    ) {
        imageView.setImageResource(image)
        textView.setTextColor(color)
    }

    private fun setDefaultColorIcons() {
        binding.homeIV.setImageResource(R.drawable.ic_home)
        binding.rideIV.setImageResource(R.drawable.ic_my_rides)
        binding.profileIV.setImageResource(R.drawable.ic_profile)
        binding.paymentIV.setImageResource(R.drawable.ic_paymwent_method)
        binding.aboutIV.setImageResource(R.drawable.ic_about_us)
        binding.privacyIV.setImageResource(R.drawable.ic_privacy_policy)
        binding.termsIV.setImageResource(R.drawable.ic_term_condition)
        binding.contactUsIV.setImageResource(R.drawable.ic_contact_us)

        binding.homeTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
        binding.rideTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
        binding.profileTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
        binding.paymentTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
        binding.aboutTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
        binding.privacyTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
        binding.termsTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
        binding.contactUsTV.setTextColor(ContextCompat.getColor(this, R.color._82C9EF))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val destArray = listOf<Int>(
            R.id.homeFragment,
            R.id.action_select_payment,
            R.id.action_profile_fragment,
            R.id.action_booking_request,
            R.id.staticPagesFragment,
            R.id.contactUsFragment
        )
        if (destArray.contains(getCurrentFragmentId())) {
            if (!binding.drawer.isDrawerOpen(GravityCompat.END)) {
                openDrawer()
            } else {
                binding.drawer.close()
            }

        } else {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCurrentFragmentId(): Int? {
        return navController?.currentDestination?.id
    }


    fun setToolbar(title: String?) {
        binding.titleTV.visibleView(!title.isNullOrEmpty())
        binding.titleTV.text = title
    }

    fun getCurrentFragment(): Fragment {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment)
        return navHostFragment!!.childFragmentManager.fragments[0]
    }

    private fun logoutObserveData() {
        viewModel.logoutResponseLiveData.observe(this) {
            it.getContentIfNotHandled()?.let {
                when (it) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        viewModel.saveToken("")
                        viewModel.clearAllData()
                        goToInitialActivity()
                    }
                    is DataResult.Failure -> {
                        handleFailure(it.message, it.exception, it.errorCode)
                    }
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        editor?.putString("page_open", "")?.apply()
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        } catch (e: Exception) {
            showException(e)
        }
    }

    override fun onResume() {
        super.onResume()
        val intentNotification = IntentFilter()
        intentNotification.addAction(Constants.DISPLAY_MESSAGE_ACTION)
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentNotification)
        } catch (e: Exception) {
            showException(e)
        }
        val intent = IntentFilter()
        intent.addAction(Constants.DISPLAY_MESSAGE_ACTION)
    }

    override fun onBackPressed() {
        val destArray = listOf<Int>(R.id.homeFragment)
        val backPressAry = listOf<Int>(
            R.id.homeFragment,
            R.id.action_select_payment,
            R.id.action_profile_fragment,
            R.id.action_booking_request,
            R.id.staticPagesFragment,
            R.id.contactUsFragment
        )
        if (destArray.contains(getCurrentFragmentId())) {
            // check for view  to open
            when (preferences?.getString("page_open", "")) {
                "set_vehicle_type" -> {
                    viewModelClick.selectItem("set_vehicle_type")
                }
                "set_ride_info" -> {
                    viewModelClick.selectItem("set_ride_info")
                }
                else -> {
                    backAction()
                }
            }
        } else if (backPressAry.contains(getCurrentFragmentId())) {
            navController?.navigate(R.id.homeFragment)
        } else {
            navController?.popBackStack()
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ivNotification -> {
                navController?.navigate(R.id.action_home_to_notification)
            }
            R.id.optionCL -> {

            }
            R.id.drawerOptionCL, R.id.drawerCL -> {
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.ivMenu -> {
                openDrawer()
            }
            R.id.homeCL -> {
                setDefaultColorIcons()
                showOpenFragmentHighLighted()
                binding.titleTV.text = getString(R.string.home)
                navController?.navigate(R.id.homeFragment)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.rideCL -> {
                setDefaultColorIcons()
                showOpenFragmentHighLighted()
                binding.titleTV.text = getString(R.string.my_rides)
                val bundle = Bundle()
                bundle.putString("page_from", Constants.HOME)
                navController?.navigate(R.id.action_booking_request, bundle)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.profileCL -> {
                setDefaultColorIcons()
                showOpenFragmentHighLighted()
                binding.titleTV.text = getString(R.string.my_profile)
                navController?.navigate(R.id.action_profile_fragment)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.paymentCL -> {
                setDefaultColorIcons()
                showOpenFragmentHighLighted()
                val bundle = Bundle()
                bundle.putString("page_from", Constants.HOME)
                navController?.navigate(R.id.action_select_payment, bundle)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.aboutCL -> {
                binding.titleTV.text = getString(R.string.about_us)
                val bundle = Bundle()
                bundle.putString("type", Constants.ABOUT_US)
                bundle.putString("page_from", Constants.HOME)
                navController?.navigate(R.id.staticPagesFragment, bundle)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.privacyCL -> {
                binding.titleTV.text = getString(R.string.privacy_policy)
                val bundle = Bundle()
                bundle.putString("type", Constants.PRIVACY)
                bundle.putString("page_from", Constants.HOME)
                navController?.navigate(R.id.staticPagesFragment, bundle)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.termsConditionsCL -> {
                binding.titleTV.text = getString(R.string.terms_a_conditions)
                val bundle = Bundle()
                bundle.putString("type", Constants.TERMSANDCONTION)
                bundle.putString("page_from", Constants.HOME)
                navController?.navigate(R.id.staticPagesFragment, bundle)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.contactUsCL -> {
                binding.titleTV.text = getString(R.string.contact_us)
                navController?.navigate(R.id.contactUsFragment)
                binding.drawer.closeDrawer(GravityCompat.END)
            }
            R.id.logoutIV -> {
                showLogoutDialog()
            }
        }

    }

    private fun showLogoutDialog() {
        dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_logout)
        dialog?.setCancelable(true)
        val cancelBT = dialog?.findViewById(R.id.cancelBT) as AppCompatTextView
        val logoutBT = dialog?.findViewById(R.id.logoutBT) as AppCompatTextView
        val logoutCL = dialog?.findViewById(R.id.logoutCL) as ConstraintLayout
        val card = dialog?.findViewById(R.id.card) as CardView
        card.setOnClickListener {

        }
        logoutCL.setOnClickListener {
            dialog?.dismiss()
        }
        cancelBT.setOnClickListener {
            dialog?.dismiss()
        }
        logoutBT.setOnClickListener {
            dialog?.dismiss()
            viewModel.onClickLogout()
            binding.drawer.closeDrawer(GravityCompat.END)
        }
        dialog?.show()
    }

    /********************************** Notification **************/
    // notification broadcast receiver
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Constants.DISPLAY_MESSAGE_ACTION -> {
                    val bundle = intent.getBundleExtra("detail")
                    if (bundle!!.containsKey("type")) {

                        when (bundle.getString("type")!!) {
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_BOOK.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_ACCEPTED.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_ONGOING.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_REJECTED.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_COMPLETED.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_PICKUP.value.toString(),
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_AS_REACHED.value.toString() -> {
                                val fragmentList = listOf(R.id.homeFragment)
                                if (fragmentList.contains(getCurrentFragmentId())) {
                                    if (bundle.getString("type")!! == Constants.NotificationType.NOTIFICATION_TYPE_RIDE_COMPLETED.value.toString()) {
                                        viewModelClick.selectItem("call_ride_info")
                                    }
                                    clearNotification()
                                }
                            }
                            Constants.NotificationType.NOTIFICATION_TYPE_RIDE_AUTO_CANCEL.value.toString() -> {
                                clearNotification()
                                Prefs.save(Constants.RIDE_ID, "")
                            }
                        }
                    } else {
                        openNotificationDialog(bundle)
                    }

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun opeDialog(type: String = "") {
        noDriverDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        noDriverDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        noDriverDialog?.setContentView(R.layout.dialog_logout)
        noDriverDialog?.setCancelable(true)
        val cancelBT = noDriverDialog?.findViewById(R.id.cancelBT) as AppCompatTextView
        val logoutBT = noDriverDialog?.findViewById(R.id.logoutBT) as AppCompatTextView
        val titleTV = noDriverDialog?.findViewById(R.id.titleTV) as AppCompatTextView
        val logoutCL = noDriverDialog?.findViewById(R.id.logoutCL) as ConstraintLayout
        cancelBT.visibleView(false)
        titleTV.text = "No driver found, your ride is cancelled. Try again later."
        logoutBT.text = getString(R.string.ok)
        val card = noDriverDialog?.findViewById(R.id.card) as CardView
        card.setOnClickListener {

        }
        logoutCL.setOnClickListener {
            noDriverDialog?.dismiss()
        }
        cancelBT.setOnClickListener {
            noDriverDialog?.dismiss()
        }
        logoutBT.setOnClickListener {
            noDriverDialog?.dismiss()

        }
        noDriverDialog?.show()
    }

    private fun openNotificationDialog(bundle: Bundle) {
        showAlertDialog(message = (bundle.get("title") as String),
            title = getString(R.string.app_name),
            postiveBtnText = getString(android.R.string.ok),
            negativeBtnText = getString(R.string.dismiss).toUpperCase(Locale.getDefault()),
            handleClick = { positiveClick ->
                if (positiveClick) {
//                    checkAction(bundle, false)

                } else {
                    clearNotification()
                }
            })

    }


    fun clearNotification() {
        val nMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        nMgr?.cancelAll()
    }

    override fun onDestroy() {
        Prefs.getString(Constants.COUNTRY_CODE, "")
        super.onDestroy()
    }

}
