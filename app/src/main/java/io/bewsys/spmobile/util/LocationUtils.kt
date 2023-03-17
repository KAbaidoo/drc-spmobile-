package io.bewsys.spmobile.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationProvider(val context: Context) {
    private var location: MutableLiveData<Location> = MutableLiveData()
    private var getFusedLocationProviderClient: FusedLocationProviderClient? =null

    // using singleton pattern to get the locationProviderClient
    init {
        getFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): LiveData<Location> {

        getFusedLocationProviderClient?.lastLocation
            ?.addOnSuccessListener { loc: Location? ->
                location.value = loc
            }
        return location
    }
}