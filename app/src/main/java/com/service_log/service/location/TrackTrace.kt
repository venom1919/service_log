package com.service_log.service.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.service_log.dao.TripDao
import com.service_log.db.TripDB
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackTrace(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var roomDatabase: TripDB
    private var dao: TripDao

    lateinit var mLocationClient: GoogleApiClient
    private val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    var mLocationRequest = LocationRequest()


    var context:Context

    init {
        this.context = context
        initial(context)

        roomDatabase = Room.databaseBuilder(
            context,
            TripDB::class.java,
            "TripService.db"
        ).fallbackToDestructiveMigration().build()

        dao = roomDatabase.tripDAO()
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

    fun isConnect(){
    /////
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("faileddd", "")
    }

    override fun onConnected(p0: Bundle?) {
        Log.i("connceter", "")

//        CoroutineScope(Dispatchers.IO).launch {
//            Log.i("coroutiness","")
//            dao.insertAll(Trip(imei="488658696", type = TypeEvent.LOCATION, details = "test", date = "45567"))
//        }

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

            Log.i("ds23232", "333")
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
        p0?.let { Log.i("provikk", it.provider) }
        dao.insertAll(Trip(1, "488658696", TypeEvent.LOCATION, "test", "20122022"))

        Log.i("tatatacx", p0?.latitude.toString() + " " + p0?.longitude.toString())
    }


}