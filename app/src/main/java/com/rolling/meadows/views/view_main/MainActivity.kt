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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
                        //navController?.navigate(R.id., bundleIntent)
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val destArray = listOf<Int>(
            R.id.homeFragment,
            R.id.staticPagesFragment,
        )
        if (destArray.contains(getCurrentFragmentId())) {
            if (!binding.drawer.isDrawerOpen(GravityCompat.END)) {
//                openDrawer()
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
        if (destArray.contains(getCurrentFragmentId())) {
            // check for view  to open
            backAction()
        }  else {
            navController?.popBackStack()
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {

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
