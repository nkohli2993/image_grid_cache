package com.rolling.meadows.base

import CommonFunctions
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.lassi.common.utils.KeyUtils
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.LassiOption
import com.lassi.presentation.builder.Lassi
import com.rolling.meadows.BuildConfig
import com.rolling.meadows.R
import com.rolling.meadows.cache.CacheConstants
import com.rolling.meadows.cache.Prefs
import com.rolling.meadows.network.HTTPStatus
import com.rolling.meadows.network.retrofit.ApiException
import com.rolling.meadows.utils.ImageUtils
import com.rolling.meadows.utils.extensions.showError
import com.rolling.meadows.utils.extensions.visibleView
import com.rolling.meadows.view_model.LoginViewModel
import com.rolling.meadows.views.authentication.InitialActivity
import com.rolling.meadows.views.view_main.MainActivity
import com.wang.avi.AVLoadingIndicatorView
import org.koin.android.ext.android.inject
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

abstract class BaseActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    var selectedFile: MutableLiveData<File> = MutableLiveData()
    private val PERMISSION_REQUEST_CODE = 200
    private val PERMISSION_REQUEST_CODE_POST_NOTIFICATIONS = 700
    fun hideKeyBoard(input: View?) {

        input?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(input.windowToken, 0)
        }
    }

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager
                .activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressLoader()
        FirebaseApp.initializeApp(this)
        getFirebaseToken()
    }

    fun hideKeyBoard(): Boolean {
        try {
            if (currentFocus != null) {
                val inputMethodManager =
                    this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?

                inputMethodManager?.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun setFocus(editText: EditText?) {
        if (editText == null) return
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
    }

    fun showSoftKeyboard(editText: EditText?) {
        if (editText == null) return
        val imm = getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }

    fun handleError(errorInt: Int?, errorString: String?) {
        errorInt?.let {
            Toast.makeText(this, getString(errorInt), Toast.LENGTH_SHORT).show()

        }
        errorString?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageUtils by inject<ImageUtils>()
        imageUtils.onActivityResult(requestCode, resultCode, data)

    }

    fun handleFailure(
        message: String? = null,
        exception: Throwable? = null,
        errorCode: Int? = null
    ) {
        hideLoading()
        if (this.isFinishing) return
        exception?.let {
            when (it) {
                is SocketTimeoutException, is TimeoutException, is IOException -> {
                    showError(this, "There is some issue in network, please try again")
                }
                !is ApiException -> {
                    showError(this, message!!)
                }
            }
        } ?: run {
            when (errorCode) {
                HTTPStatus.UNAUTHORIZED_AND_VALIDATIONS -> {
                    showError(this, message!!)
                    viewModel.saveToken("")
                    viewModel.clearAllData()
                    goToInitialActivity()
                }
                HTTPStatus.NOT_FOUND -> {
                    showError(this, message!!)
                }
                HTTPStatus.NETWORK_ISSUE -> {
                    showError(this, "Internet Connection Not Found")
                }
                else -> {
                    showError(this, message!!)
                }
            }
        }

    }

/*
    private fun showSessionTimeOutDialog(message: String?) {
        val dialog = Dialog(this, android.R.style.Theme_Panel)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.setCancelable(false)
        val cancelBT = dialog.findViewById(R.id.cancelBT) as AppCompatTextView
        val logoutBT = dialog.findViewById(R.id.logoutBT) as AppCompatTextView
        val titleTV = dialog.findViewById(R.id.titleTV) as AppCompatTextView
        cancelBT.visibleView(false)
        titleTV.text = message
        logoutBT.text = getString(R.string.log_in)
        logoutBT.setOnClickListener {
            dialog.dismiss()
            Prefs.save(CacheConstants.USER_TOKEN, null)
            goToInitialActivity()
        }
        dialog.show()
    }
*/


    fun initProgressLoader() {
        progressDialog = Dialog(this)
        val view = View.inflate(this, R.layout.view_loading_circular, null)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(view)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setCancelable(false)
    }

    private var progressDialog: Dialog? = null
    private var loadingAlertDialog: AVLoadingIndicatorView? = null


    fun showLoading(message: String? = getString(R.string.loading_please_wait)) {
        if (progressDialog == null) {
            initProgressLoader()
        }
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
        progressDialog?.show()
        loadingAlertDialog?.show()
    }

    fun hideLoading() {
        progressDialog?.dismiss()
        loadingAlertDialog?.hide()
    }

    private var toast: Toast? = null
    fun showToast(message: String?) {
        hideKeyBoard()
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun goToInitialActivity() {
        val intent = Intent(this, InitialActivity::class.java)
        intent.putExtra("page_open", "login")
        startActivity(intent)
    }

    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun getUniqueDeviceId(): String? {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            if (BuildConfig.DEBUG) {
                Log.e("catch_token", "token: ${task.result?.toString()}")
            }
            viewModel.saveDeviceToken(task.result?.toString())
        }

    }


    private var exit: Boolean = false

    fun backAction() {
        if (exit) {
            finishAffinity()
        } else {
            Toast.makeText(this, getString(R.string.press_one_more_time), Toast.LENGTH_SHORT).show()
            exit = true
            Handler().postDelayed({ exit = false }, (2 * 1000).toLong())
        }
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED
                if (readAccepted && writeAccepted && cameraAccepted)
                    openImagePicker()
                else {
                    // showError(this,"Permission Denied, You cannot access Gallery data and Camera.")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val builder = AlertDialog.Builder(this)
                        val dialog = builder.apply {
                            setMessage("Permission Denied, You need to allow Gallery data and Camera permissions")
                            setPositiveButton("OK") { _, _ ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //open app setting to access
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    val uri: Uri =
                                        Uri.fromParts(
                                            "package",
                                            applicationContext.packageName,
                                            null
                                        )
                                    intent.data = uri
                                    context.startActivity(intent)
                                }

                            }
                            setNegativeButton("Cancel") { _, _ ->

                            }
                        }.create()
                        dialog.show()
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                        return
                    }
                }
            }
            PERMISSION_REQUEST_CODE_POST_NOTIFICATIONS -> {
                if (grantResults.isNotEmpty()) {
                    val postNotificationPermissionAccepted =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (postNotificationPermissionAccepted) {
                        return
                    }
                }

            }

        }

    }

    fun checkPermission(): Boolean {
        val readStorageresult =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val writeStorageresult =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val cameraResult = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        )
        return readStorageresult == PackageManager.PERMISSION_GRANTED && writeStorageresult == PackageManager.PERMISSION_GRANTED && cameraResult == PackageManager.PERMISSION_GRANTED
    }

    fun openImagePicker() {
        val intent = Lassi(this)
            .with(LassiOption.CAMERA_AND_GALLERY)
            .setMaxCount(1)
            .setGridSize(3)
            .setPlaceHolder(com.lassi.R.drawable.ic_image_placeholder)
            .setErrorDrawable(com.lassi.R.drawable.ic_image_placeholder)
            .setSelectionDrawable(com.lassi.R.drawable.ic_checked_media)
            .setStatusBarColor(R.color.colorPrimaryDark)
            .setToolbarColor(R.color.colorPrimary)
            .setToolbarResourceColor(android.R.color.white)
            .setProgressBarColor(R.color.colorAccent)
            .setCompressionRation(10)
            .setMinFileSize(0)
            .setMaxFileSize(2000)
            .setSupportedFileTypes("jpg", "jpeg", "png", "webp", "gif")
            .enableFlip()
            .enableRotate()
            .build()
        receiveData.launch(intent)
    }

    private val receiveData =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedMedia =
                    it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
                if (!selectedMedia.isNullOrEmpty()) {
                    var file: File? = null
                    file = if (selectedMedia[0].path?.startsWith("content")!!) {
                        CommonFunctions.fileFromContentUri(
                            this@BaseActivity,
                            selectedMedia[0].path.toString().toUri()
                        )
                    } else {
                        File(selectedMedia[0].path!!)
                    }
                    selectedFile.value = file!!
                }
            }
        }

    fun updateStatusBarColor(
        @ColorRes colorId: Int,
        isStatusBarFontDark: Boolean = true,
        statusBarColor: Int = R.color.transparent
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            window.navigationBarColor = ContextCompat.getColor(this, statusBarColor)
            window.setBackgroundDrawable(ContextCompat.getDrawable(this, colorId))
            setSystemBarTheme(isStatusBarFontDark)
        }
    }

    private fun setSystemBarTheme(isStatusBarFontDark: Boolean) {
        window.decorView.systemUiVisibility =
            if (isStatusBarFontDark) {
                0
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestPostNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
            ),
            PERMISSION_REQUEST_CODE_POST_NOTIFICATIONS
        )
    }

    fun announcementBackgroundList():ArrayList<Int>{
        val announcementBackgroundList =
            resources.obtainTypedArray(R.array.background_announcements)
        val list :ArrayList<Int> = arrayListOf()
        for (i in 0 until 4) {
            list.add(announcementBackgroundList.getResourceId(i, -1))
        }
        announcementBackgroundList.recycle()
        return list
    }
    fun eventBackgroundList():ArrayList<Int>{
        val eventBackgroundList =
            resources.obtainTypedArray(R.array.background_events)
        val list :ArrayList<Int> = arrayListOf()
        for (i in 0 until 4) {
            list.add(eventBackgroundList.getResourceId(i, -1))
        }
        eventBackgroundList.recycle()
        return list
    }
    fun menuBackgroundList():ArrayList<Int>{
        val menuBackgroundList =
            resources.obtainTypedArray(R.array.background_menu)
        val list :ArrayList<Int> = arrayListOf()
        for (i in 0 until 4) {
            list.add(menuBackgroundList.getResourceId(i, -1))
        }
        menuBackgroundList.recycle()
        return list
    }
}