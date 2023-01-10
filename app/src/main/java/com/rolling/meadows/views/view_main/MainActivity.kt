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
import androidx.navigation.fragment.findNavController
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
            if (bundle.getString("event_id") != null) {
                val notificationBundle = Bundle()
                notificationBundle.putInt("event_id", bundle.getString("event_id")!!.toInt())
                navController?.navigate(R.id.home_to_rolling_detail,notificationBundle)
            } else {
                navController?.navigate(R.id.homeFragment)
            }
        } else {
            goToInitialActivity()
        }
        clearNotification()
    }


    fun getCurrentFragmentId(): Int? {
        return navController?.currentDestination?.id
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
        val cancelBT = dialog?.findViewById(R.id.canelBT) as AppCompatTextView
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
//            viewModel.onClickLogout()
            goToInitialActivity()
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
                    } else {
                        openNotificationDialog(bundle)
                    }

                }
            }
        }
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
        super.onDestroy()
    }

}
