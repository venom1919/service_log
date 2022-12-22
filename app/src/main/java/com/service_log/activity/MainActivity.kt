package com.service_log.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.service_log.R
import com.service_log.service.GeneralService

open class MainActivity: AppCompatActivity(){

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
///            lis -> lis.
//        }
        //////2------------

    }
}