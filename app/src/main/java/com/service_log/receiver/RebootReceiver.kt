package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.repository.TripRepository
import com.service_log.service.GeneralService
import com.service_log.service.static.AssignmentHelper

class RebootReceiver : BroadcastReceiver() {

    private lateinit var dao: TripRepository

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(p0: Context?, p1: Intent?) {

        dao = TripRepository(p0!!)
        dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(p0), type = TypeEvent.REBOOT, details = "phone was reboot", date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))
        val i = Intent(p0, GeneralService::class.java)

//        val pac: PackageManager = p0.packageManager
//        val launchIntent = pac.getLaunchIntentForPackage("com.treedo.taburetka.tsd")
//        p0.startActivity(launchIntent)


        p0.startService(i)

    }
}