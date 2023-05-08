package io.bewsys.spmobile.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient

class LocationProvider(context: Context) {
    private var getFusedLocationProviderClient: FusedLocationProviderClient? =
        getFusedLocationProviderClient(context)

    private val _location = MutableLiveData<Location>()
    val location:LiveData<Location>
        get() = _location

    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        getFusedLocationProviderClient?.lastLocation
            ?.addOnSuccessListener { loc: Location? ->
                _location.value = loc
            }
    }
}