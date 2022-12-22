package com.service_log.service.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.service_log.repository.TripRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TrackTrace(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var dao: TripRepository
    lateinit var mLocationClient: GoogleApiClient
    private val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    var mLocationRequest = LocationRequest()
    var context: Context
    var latitude = 0.0
    var longitude = 0.0

    private var locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

    private var mFusedLocationClient: FusedLocationProviderClient

    init {

        this.context = context
        initial(context)
        dao = TripRepository(context)
        mFusedLocationClient = FusedLocationProviderClient(context)

    }

    private fun initial(context: Context){
        mLocationClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 0

        mLocationRequest.priority = priority
        mLocationClient.connect()

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("faileddd", "")
    }

    override fun onConnected(p0: Bundle?) {
        getLocation()
        mFusedLocationClient.lastLocation.addOnSuccessListener {

            getLocation(context)

        }

        Log.i("connceter", "")

        CoroutineScope(Dispatchers.IO).launch {
            Log.i("coroutiness","")

            var p = dao.getAllTrip()
            for (trip in p){
                    Log.i("ssssss2222", trip.id.toString())
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mLocationClient,
            mLocationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i("xcxqe23", "xzx")
    }

    override fun onLocationChanged(p0: android.location.Location?) {

        val loc = p0?.latitude.toString() + " " + p0?.longitude.toString()
        calculateDistance(latitude, longitude, p0!!.longitude, p0!!.latitude)
//      latitude = p0.latitude
//      longitude = p0.longitude
        val dataLongLat: ArrayList<Double>

        Log.i("accurccj", p0?.getAccuracy().toString())
        Log.i("speeddd", p0?.speed.toString())
        Log.i("prrr", p0?.provider.toString())
        Log.i("pccc", p0?.getSpeed().toString())
        Log.i("tatatacx", p0?.getAccuracy().toString() + " " + p0?.latitude.toString() + " " + p0?.longitude.toString())

        val data = dao.getAllTrip()
        p0.reset()

        data.forEach {
            Log.i("detete", it.details)
        }
    }

    fun correctFilterDistance(): Boolean{
        return false
    }

    private fun calculateDistance(lat:Double, lon:Double, lat2:Double, lon2:Double ){

        val R = 6371; // km
        val dLat = (lat-lat)
        val dLon = (lon-lon)
        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat) * Math.cos(lat) *
                Math.sin(dLon/2) * Math.sin(dLon/2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        var d = R * c

        Log.i("sssxc" , d.toString())

        latitude = lat2
        longitude = lon2

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation(context: Context) {
//        locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER
//        mFusedLocationClient.lastLocation.addOnSuccessListener({})

                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->

                    var location: Location? = task.result
                        Log.i("listnern", location?.latitude.toString() + " " + location?.longitude.toString())

                }

//        mFusedLocationClient.requestLocationUpdates()

    //        mFusedLocationClient.requestLocationUpdates()
    }

//    private fun requestCurrentLocation(context: Context): DoubleArray? {
//        val fusedLocationClient: FusedLocationProviderClient by lazy {
//            LocationServices.getFusedLocationProviderClient(context)
//        }
//        var cancellationTokenSource = CancellationTokenSource()
//        val gps = DoubleArray(2)
//
//
//    }

}


