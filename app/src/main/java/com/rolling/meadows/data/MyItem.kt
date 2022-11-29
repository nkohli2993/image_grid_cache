package com.rolling.meadows.data
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyItem :ClusterItem{
    var lat :Double =0.0
    var long :Double=0.0
    override fun getPosition(): LatLng {
        return LatLng(lat,long)
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getSnippet(): String {
        return ""
    }

}
