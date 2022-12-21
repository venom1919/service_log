package com.service_log.service.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.service_log.repository.TripRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackTrace(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var dao: TripRepository
    lateinit var mLocationClient: GoogleApiClient
    private val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    var mLocationRequest = LocationRequest()
    var context: Context
    var latitude = 0.0
    var longitude = 0.0

    init {

        this.context = context
        initial(context)
        dao = TripRepository(context)

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
        latitude = p0.latitude
        longitude = p0.longitude

        val dataLongLat: ArrayList<Double>

//      dao.insertTrip(Trip(imei= "488658696", type = TypeEvent.LOCATION, details = loc, date = "20122022"))
        Log.i("accurccj", p0?.getAccuracy().toString())
        Log.i("speeddd", p0?.speed.toString())
        Log.i("prrr", p0?.provider.toString())
        Log.i("pccc", p0?.getSpeed().toString())
        Log.i("tatatacx", p0?.getAccuracy().toString() + " " + p0?.latitude.toString() + " " + p0?.longitude.toString())

        val data = dao.getAllTrip()

//        dataLongLat = ArrayList()
//        dataLongLat.add(p0?.latitude!!)

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

        return

       }
    }


