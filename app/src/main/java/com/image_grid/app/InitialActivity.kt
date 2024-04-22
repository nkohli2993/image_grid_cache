package com.image_grid.app

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.image_grid.app.data.ImageData
import com.image_grid.app.network.retrofit.DataResult
import com.image_grid.app.network.retrofit.observeEvent
import com.image_grid.app.view_model.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InitialActivity : AppCompatActivity() {
    private lateinit var binding: com.image_grid.app.databinding.ActivityInitialBinding
    private var imageLoader: ImageLoader? = null
    private var progressDialog: Dialog? = null
    private val viewModel: ImageViewModel by viewModels()
    private var imageList: ArrayList<ImageData> = arrayListOf()

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
            viewModel.getImageList()
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

}