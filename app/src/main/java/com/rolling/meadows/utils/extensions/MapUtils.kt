package com.rolling.meadows.utils.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.util.*


fun String.toLocation(provider: String): Location? {
    val components = this.split(",")
    if (components.size != 2)
        return null

    val lat = components[0].toDoubleOrNull() ?: return null
    val lng = components[1].toDoubleOrNull() ?: return null
    val location = Location(provider);
    location.latitude = lat
    location.longitude = lng
    return location
}

fun Context.directionsTo(location: Location) {
    val lat = location.latitude
    val lng = location.longitude
    val uri = String.format(Locale.US, "http://maps.google.com/maps?daddr=%f,%f", lat, lng)
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }
    catch (e: ActivityNotFoundException) {
        e.printStackTrace()

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }
}




fun Context.isGPSEnabled() = (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
    LocationManager.GPS_PROVIDER
)

 fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}