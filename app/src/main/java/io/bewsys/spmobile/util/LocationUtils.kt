package io.bewsys.spmobile.util

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient

//class LocationProvider(context: Context) {
//    private var getFusedLocationProviderClient: FusedLocationProviderClient? =
//        getFusedLocationProviderClient(context)
//
//    private val _location = MutableLiveData<Location>()
//    val location: LiveData<Location>
//        get() = _location
//
//    init {
//        getLocation()
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun getLocation() {
//        getFusedLocationProviderClient?.lastLocation
//            ?.addOnSuccessListener { loc: Location? ->
//                _location.value = loc
//            }
//    }
//}

class LocationProvider(context: Context) :Service(), LocationListener {

    private val locationManager: LocationManager =
        context.getSystemService(LOCATION_SERVICE) as LocationManager
    var checkGPS = false
//    var checkNetwork = false
//    var canGetLocation = false


    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location


    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        try {
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (checkGPS ) {
//            if (checkGPS && checkNetwork) {
//                canGetLocation = true
                if (checkGPS) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this
                    )
                    _location.value =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                }

            }
        }catch (e :Exception){
            e.printStackTrace()
        }

    }

    fun removeLocationUpdates(){
        if (locationManager != null){
            locationManager.removeUpdates(this)
        }
    }
    override fun onLocationChanged(locaton: Location) {

    }
    override fun onBind(p0: Intent?): IBinder? {
       return null
    }



    companion object {
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10F
        const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1L
    }




}