package io.bewsys.spmobile.util

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient



class LocationProvider(context: Context) : Service(), LocationListener {

    private val locationManager: LocationManager =
        context.getSystemService(LOCATION_SERVICE) as LocationManager
    var checkGPS = false
//    var checkNetwork = false
//    var canGetLocation = false

//
//    private val _location = MutableLiveData<Location>()
//    val location: LiveData<Location>
//        get() = _location




    @SuppressLint("MissingPermission")
    fun getLocation(onLocationProvided:(Location)-> Unit) {
        try {
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (checkGPS) {
//            if (checkGPS && checkNetwork) {
//                canGetLocation = true
                if (checkGPS) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                    )


                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?.let { onLocationProvided(it) }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removeLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(this)
        }
    }

    override fun onLocationChanged(locaton: Location) {

    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        super.onStatusChanged(provider, status, extras)
    }

    companion object {
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10F
        const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1L
    }


}
