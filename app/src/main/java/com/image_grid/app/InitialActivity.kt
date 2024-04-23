package com.image_grid.app

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.image_grid.app.data.ImageData
import com.image_grid.app.network.retrofit.DataResult
import com.image_grid.app.network.retrofit.observeEvent
import com.image_grid.app.view_model.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InitialActivity : AppCompatActivity(), PermCallback {
    private lateinit var binding: com.image_grid.app.databinding.ActivityInitialBinding
    private var imageLoader: ImageLoader? = null
    private var progressDialog: Dialog? = null
    private val viewModel: ImageViewModel by viewModels()
    private var imageList: ArrayList<ImageData> = arrayListOf()
    private var permCallback: PermCallback? = null
    private var reqCode: Int = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_initial)
        imageLoader = ImageLoader(this)
        val adapter = ActivityAdapter(imageList, imageLoader!!)
        binding.imageRL.adapter = adapter
        viewModel.imageResponseLiveData.observeEvent(this) { result ->
            when (result) {
                is DataResult.Failure -> {
                    hideLoading()
                    Toast.makeText(this, result.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is DataResult.Loading -> {
                    showLoading()
                }

                is DataResult.Success -> {
                    hideLoading()
                    imageList.addAll(result.data!!)
                    adapter.notifyDataSetChanged()
                    if (imageList.size > 0) {
                        binding.imageRL.visibility = View.VISIBLE
                    } else {
                        binding.imageRL.visibility = View.GONE
                    }
                }

                else -> {}
            }
        }
        viewModel.limit.value = 100
        if (isNetworkAvailable(this)) {
            if (checkPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    99,
                    this) && SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                viewModel.getImageList()
            }else{
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts(
                    "package",
                    packageName, null
                )
                intent.setData(uri)
                startActivity(intent)
            }
        } else {
            Toast.makeText(
                this,
                "No internet connection found, Please try again later",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    private fun initProgressLoader() {
        progressDialog = Dialog(this)
        val view = View.inflate(this, R.layout.view_loading_circular, null)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(view)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setCancelable(false)
    }

    private fun showLoading() {
        if (progressDialog == null) {
            initProgressLoader()
        }
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
        progressDialog?.show()
    }

    private fun hideLoading() {
        progressDialog?.dismiss()
    }



    fun checkPermissions(perms: Array<String>, requestCode: Int, permCallback: PermCallback): Boolean {
        this.permCallback = permCallback
        this.reqCode = requestCode
        val permsArray = java.util.ArrayList<String>()
        var hasPerms = true
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                permsArray.add(perm)
                hasPerms = false
            }
        }
        if (!hasPerms) {
            val permsString = arrayOfNulls<String>(permsArray.size)
            for (i in permsArray.indices) {
                permsString[i] = permsArray[i]
            }
            ActivityCompat.requestPermissions(this  , permsString,99)
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permGrantedBool = false
        when (requestCode) {
           99 -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this@InitialActivity,"Not sufficient permissions \n Goto Settings for " + getString(R.string.app_name)
                                + " Permissions",Toast.LENGTH_SHORT).show()
                        permGrantedBool = false
                        break
                    } else {
                        permGrantedBool = true
                    }
                }
                if (permCallback != null) {
                    if (permGrantedBool) {
                        permCallback!!.permGranted(reqCode)
                    } else {
                        permCallback!!.permDenied(reqCode)
                    }
                }
            }
        }
    }

    override fun permGranted(resultCode: Int) {
        if(isNetworkAvailable(this@InitialActivity)){
            viewModel.getImageList()
        }else{
            Toast.makeText(
                this,
                "No internet connection found, Please try again later",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun permDenied(resultCode: Int) {
        Toast.makeText(this@InitialActivity,"Not sufficient permissions \n Goto Settings for " + getString(R.string.app_name)
                + " Permissions",Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}