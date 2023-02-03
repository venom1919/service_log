package com.service_log.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class CurrentLocationComponent(context: Context){

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private var cancellationTokenSource = CancellationTokenSource()

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                Log.i("ccc1q2", location.latitude.toString())
            } else {
//                locationErrorCallback("Location not found")
            }
        }.addOnFailureListener { exception ->
            exception.localizedMessage?.let {
//                locationErrorCallback(it)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
//        locationErrorCallback("Location request cancelled")

        //cancel ongoing location request
        cancellationTokenSource.cancel()

        //re-initialize new cancellation token source
        cancellationTokenSource = CancellationTokenSource()
    }

}