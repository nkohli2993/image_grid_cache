package com.rolling.meadows.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.rolling.meadows.BuildConfig
import com.rolling.meadows.R
import com.rolling.meadows.utils.extensions.showLog
import kotlinx.coroutines.DelicateCoroutinesApi
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class GoogleApisHandle {
    private var routeMap: GoogleMap? = null
    internal var context: Context? = null
    private var origin: LatLng? = null
    private var destination: LatLng? = null
    private var isAddLine: Boolean = false
    private var isCameraMoveByCustomer: Boolean = false
    private var polyline: Polyline? = null
    private var totalDistance: Double? = null
    private var onPolyLineReceived: OnPolyLineReceived? = null
    private var rideTimeView: AppCompatTextView? = null
    private fun setAct(mAct: Context) {
        this.context = mAct
    }

    fun getLastKnownLocation(context: Context): Location? {
        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    context,
                    context.getString(R.string.location_permission_not_specified),
                    Toast.LENGTH_LONG
                ).show()
                return bestLocation
            }
            var l: Location? = mLocationManager.getLastKnownLocation(provider)
            if (l == null) {
                mLocationManager.requestLocationUpdates(
                    provider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location) {

                        }

                        override fun onStatusChanged(
                            provider: String,
                            status: Int,
                            extras: Bundle
                        ) {

                        }

                        override fun onProviderEnabled(provider: String) {

                        }

                        override fun onProviderDisabled(provider: String) {

                        }
                    })
                l = mLocationManager.getLastKnownLocation(provider)
            }
            if (l != null && (bestLocation == null || l.accuracy > bestLocation.accuracy)) {
                bestLocation = l
            }
        }
        if (bestLocation == null && !isGPSEnabled(context)) {
            Toast.makeText(context, context.getString(R.string.gps_not_enabled), Toast.LENGTH_LONG)
                .show()
        }

        return bestLocation
    }

    private fun isGPSEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    fun getDirectionsUrl(
        origin: LatLng,
        dest: LatLng,
        googleMap: GoogleMap,
        rideTimeTV: AppCompatTextView,
        isCameraMove: Boolean
    ) {
        rideTimeView = rideTimeTV
        isAddLine = true
        isCameraMoveByCustomer = isCameraMove
        val str_origin = ("origin=" + origin.latitude + ","
                + origin.longitude)
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        val sensor = "sensor=false"
        val parameters = "$str_origin&$str_dest&$sensor"
        val output = "json"
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=${
            context!!.getString(R.string.google_key)
        }"
        showLog("direction", url)
        DownloadTask(origin, dest, googleMap).execute(url)
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            data = urlConnection.inputStream.bufferedReader().readText()
            return data

        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {

                e.printStackTrace()
            }
        } finally {
            iStream?.close()
            urlConnection?.disconnect()
        }
        return data
    }


    private interface DistanceCalculated {

        fun sendDistance(distance: Double)
    }

    private inner class DownloadTask : AsyncTask<String, Void, String> {

        constructor(source: LatLng, dest: LatLng, map: GoogleMap) {

            origin = source
            destination = dest
            routeMap = map
        }

        constructor(source: LatLng, dest: LatLng, distanceCalculated: DistanceCalculated) {

            onDistanceCalculated = distanceCalculated
            origin = source
            destination = dest
        }


        override fun doInBackground(vararg url: String): String {
            var data = ""
            try {
                data = downloadUrl(url[0])
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.d("Background Task", e.toString())
                }
            }

            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)

        }
    }

    private inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {
        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = DirectionsJSONParser()
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }

            return routes
        }

        @OptIn(DelicateCoroutinesApi::class)
        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            if (result == null) {
                return
            }
            if (result.isEmpty()) {
                return
            }
            val linePathSteps = ArrayList<LatLng>()
            for (i in result.indices) {
                val path = result[i]
                for (j in path.indices) {
                    val point = path[j]
                    linePathSteps.add(
                        LatLng(
                            java.lang.Double.parseDouble(point["lat"]!!),
                            java.lang.Double.parseDouble(point["lng"]!!)
                        )
                    )
                }
            }

            if (routeMap != null) {
                val builder = LatLngBounds.Builder()
                builder.include(LatLng(linePathSteps[0].latitude, linePathSteps[0].longitude))
                builder.include(
                    LatLng(
                        linePathSteps[linePathSteps.size - 1].latitude,
                        linePathSteps[linePathSteps.size - 1].longitude
                    )
                )
                val bounds = builder.build()
                val padding = 100
                val cu = CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    padding
                )
//                routeMap!!.moveCamera(cu)
                val lineOption = PolylineOptions()
                lineOption.points.clear()
                result.indices.forEach { _ ->
                    lineOption.addAll(linePathSteps)
                    lineOption.width(10f)
                    lineOption.color(ContextCompat.getColor(context!!, R.color._0493DF))
                }
                polyline?.remove()
                polyline = routeMap?.addPolyline(lineOption)
/*
                if (!isCameraMoveByCustomer) {
                    routeMap?.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            50
                        )
                    )
                }
*/
            }
        }
    }


    fun animateMarkerToGB(
        finalPosition: LatLng,
        oldMarker: Marker,
        isCameraAnimate: Boolean,
        googleMap: GoogleMap
    ) {
        val latLngInterpolator: LatLngInterpolator = LatLngInterpolator.Spherical()
        val startPosition = oldMarker.position
        val lat1 = startPosition.latitude
        val lng1 = startPosition.longitude
        val lat2 = finalPosition.latitude
        val lng2 = finalPosition.longitude
        val start = SystemClock.uptimeMillis()
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = 5 * 1000.toFloat()
        val handler = Handler()
        rotateMarker(lat1, lng1, lat2, lng2, oldMarker, handler)

        handler.post(object : MyMarkerRunnable(
            start,
            durationInMs,
            interpolator,
            oldMarker,
            latLngInterpolator,
            startPosition,
            finalPosition,
            isCameraAnimate,
            googleMap,
            handler
        ) {
            var elapsed: Long = 0
            var t = 0f
            var v = 0f
            override fun run() {                 // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed / durationInMs
                v = interpolator.getInterpolation(t)
                oldMarker.position = latLngInterpolator.interpolate(v, startPosition, finalPosition)
                if (isCameraAnimate) {
                    val builder = LatLngBounds.Builder()
                    builder.include(startPosition)
                    builder.include(finalPosition)
                    val bounds = builder.build()
                    val padding = 50
                    val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                    googleMap.animateCamera(cu)
                }
                //Repeat till progress is complete.
                if (t < 1) {                     // Post again 16ms later.
                    handler.postDelayed(this, 16)
                }
            }
        })
    }

    fun rotateMarker(
        fromLat: Double,
        fromLong: Double,
        toLat: Double,
        toLong: Double,
        marker: Marker,
        handler: Handler
    ) {
        val brng = bearingBetweenLocations(fromLat, fromLong, toLat, toLong)

        var rotation = marker.rotation

        if (rotation >= 360) {
            rotation %= 360
        } else if (rotation <= -360) {
            rotation %= 360
            rotation += 360
        }

        var newAngle = marker.rotation.toDouble()
        if (brng - rotation in 0.0..180.0) {
            newAngle = marker.rotation + (brng - rotation)
        } else if (brng - rotation >= 0 && brng - rotation >= 180) {
            newAngle = marker.rotation - (360 - (brng - rotation))
        } else if (brng - rotation <= 0 && brng - rotation >= -180) {
            newAngle = marker.rotation + brng - rotation
        } else if (brng - rotation <= 0 && brng - rotation <= -180) {
            newAngle = marker.rotation + (360 - (rotation - brng))
        }


        val start = SystemClock.uptimeMillis()
        val startRotation = marker.rotation
        val toRotation = newAngle.toFloat()
        val duration: Long = 1500

        val interpolator = LinearInterpolator()
        handler.post(object : MyRunnable(
            start,
            interpolator,
            duration.toFloat(),
            toRotation,
            startRotation,
            marker,
            handler
        ) {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)

                val rot = t * toRotation + (1 - t) * startRotation

                marker.rotation = if (-rot >= 180) {
                    rot / 2
                } else {
                    rot
                }
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                }
            }
        })


    }

    private open class MyMarkerRunnable(
        var start: Long,
        var durationInMs: Float,
        var interpolator: Interpolator,
        var oldMarker: Marker,
        var latLngInterpolator: LatLngInterpolator,
        var startPosition: LatLng,
        var finalPosition: LatLng,
        var isCameraAnimate: Boolean,
        var googleMap: GoogleMap,
        var handler: Handler
    ) : Runnable {
        override fun run() {}

    }

    private open class MyRunnable(
        internal var start: Long,
        internal var interpolator: Interpolator,
        internal var duration: Float,
        internal var toRotation: Float,
        internal var startRotation: Float,
        internal var marker: Marker,
        internal var handler: Handler
    ) : Runnable {

        override fun run() {

        }
    }

    private fun bearingBetweenLocations(
        fromLat: Double,
        fromLong: Double,
        toLat: Double,
        toLong: Double
    ): Double {

        val PI = 3.14159
        val lat1 = fromLat * PI / 180
        val long1 = fromLong * PI / 180
        val lat2 = toLat * PI / 180
        val long2 = toLong * PI / 180

        val dLon = long2 - long1

        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)

        var brng = Math.atan2(y, x)

        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360

        return brng
    }


    fun setPolyLineReceivedListener(onPolyLineReceived: OnPolyLineReceived) {
        this.onPolyLineReceived = onPolyLineReceived
    }

    interface OnPolyLineReceived {
        fun onPolyLineReceived(origin: LatLng?, destination: LatLng?, routeMap: GoogleMap)
    }

    companion object {
        private var mapUtils: GoogleApisHandle? = null
        private var onDistanceCalculated: DistanceCalculated? = null

        private val MIN_TIME_BW_UPDATES: Long = 1000
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10f
        fun getInstance(context: Context): GoogleApisHandle {
            if (mapUtils == null) {
                mapUtils = GoogleApisHandle()
            }
            mapUtils!!.setAct(context)
            return mapUtils!!
        }
    }


}