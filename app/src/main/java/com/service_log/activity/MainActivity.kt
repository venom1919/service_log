package com.service_log.activity

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.service_log.R
import com.service_log.receiver.LocationReceiver
import com.service_log.service.GeneralService


open class MainActivity: AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    lateinit var mLocationClient: GoogleApiClient
    var mLocationRequest = LocationRequest()

    override fun onCreate(savedInstanceState: Bundle?) {

        ////1+++++++++
        Log.i("ssssss12", "sssv")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val i = Intent(this, GeneralService::class.java)
        startService(i)
        ////1--------

        //////2++++++++++++
//        val receiver = Intent(this, LocationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, 0)
//
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        fusedLocationClient.requestLocationUpdates(LocationRequest.create(), pendingIntent).addOnSuccessListener {
////            lis -> lis.
//        }
        //////2------------

        mLocationClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 0

        val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.priority = priority
        mLocationClient.connect()

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("sss225", "")
    }

    override fun onConnected(p0: Bundle?) {
        Log.i("sss224", "")

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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

    override fun onLocationChanged(p0: Location?) {
//        Log.p0.provider
        Log.i("tutut233", p0?.latitude.toString() + " " + p0?.longitude.toString())
    }


}