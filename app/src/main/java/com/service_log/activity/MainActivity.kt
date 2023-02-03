package com.service_log.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.service_log.R
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.repository.TripRepository
import com.service_log.service.GeneralService
import com.service_log.service.static.AssignmentHelper


open class MainActivity: AppCompatActivity(){

    private lateinit var dao: TripRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

//        dao = TripRepository(this)
//        dao.updateData()

        dao = TripRepository(this)
        dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(this), type = TypeEvent.REBOOT, details = "phone was reboot", date = AssignmentHelper.retrieveDateFORMATTER(), info = "loading"))

        ////1+++++++++
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