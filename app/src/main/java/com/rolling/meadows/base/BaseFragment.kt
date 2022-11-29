package com.rolling.meadows.base

import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.rolling.meadows.utils.extensions.showLog


abstract class BaseFragment<DB : ViewDataBinding> : Fragment(),
    View.OnClickListener {

    open lateinit var binding: DB
    var baseActivity: BaseActivity? = null
    private val mMap: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_LOCATION = 199
    var isBinded = false
    abstract fun observeViewModel()
    protected abstract fun initViewBinding()

    @LayoutRes
    abstract fun getLayoutRes(): Int


    private fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        if(!isBinded){
            binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
            initViewBinding()
        }
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(!isBinded){
            init(inflater, container)
            super.onCreateView(inflater, container, savedInstanceState)
            binding.lifecycleOwner = this
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }


    override fun onClick(v: View?) {
        baseActivity?.hideKeyBoard(v)
    }


    fun showLoading(message: String? = activity?.getString(R.string.loading_please_wait)) {

        baseActivity?.showLoading(message)

    }

    fun hideLoading() {
        baseActivity?.hideLoading()
    }

    fun handleFailure(
        message: String? = null,
        exception: Throwable? = null,
        errorCode: Int? = null
    ) {
        hideLoading()
        baseActivity?.handleFailure(message, exception, errorCode)
    }

    fun showToast(message: String?) {
        baseActivity?.showToast(message)
    }

    fun setCameraOption(mLocation: Location, mMap: GoogleMap) {
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    mLocation.latitude,
                    mLocation.longitude
                )
            ) // Sets the center of the map to location user
            .zoom(15f) // Sets the zoom
            .build() // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(baseActivity!!)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {}
                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient!!.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    showLog(
                        "Location error",
                        "Location error " + connectionResult.errorCode
                    )
                }.build()
            googleApiClient?.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = (30 * 1000).toLong()
            locationRequest.fastestInterval = (5 * 1000).toLong()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(
                    googleApiClient!!,
                    builder.build()
                )
            result.setResultCallback { p0 ->
                val status: Status = p0.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(baseActivity!!, REQUEST_LOCATION)

                        //                                finish();
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }
    }

    fun changeStatusBarColor(color: Int) {
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

    fun changeStatusBarIconColor(toDark: Boolean) {
        val decor: View = requireActivity().window.decorView
        if (toDark)
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            decor.systemUiVisibility = 0
    }


    fun showCustomUI() {
        changeStatusBarColor(ContextCompat.getColor(baseActivity!!, R.color.colorPrimaryDark))
        changeStatusBarIconColor(false)
        baseActivity!!.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        baseActivity!!.window.navigationBarColor =
            ContextCompat.getColor(baseActivity!!, R.color.colorPrimary)

    }

    fun removeFlag() {
        changeStatusBarColor(ContextCompat.getColor(baseActivity!!, R.color.white))
        changeStatusBarIconColor(true)
        baseActivity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun removeFlagSplash() {
        changeStatusBarColor(ContextCompat.getColor(baseActivity!!, R.color._38C5F3))
        changeStatusBarIconColor(false)
        baseActivity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}