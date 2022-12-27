package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.repository.TripRepository
import com.service_log.service.static.AssignmentHelper

class LocationStatusReceiver : BroadcastReceiver() {

    private lateinit var dao: TripRepository

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    override fun onReceive(p0: Context?, p1: Intent?) {

        val locationStatus = AssignmentHelper.retrieveReceiverINFOByLocationButton(p0!!)

        val onOrOff: String = if (locationStatus){
            AssignmentHelper.retrieveDETAILS(TypeEvent.LOCATION_BUTTON_ON)
        }else{
            AssignmentHelper.retrieveDETAILS(TypeEvent.LOCATION_BUTTON_OFF)
        }

        Log.i("onorelss", onOrOff)

        dao = TripRepository(p0)
        dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(p0), type = if (locationStatus) TypeEvent.LOCATION_BUTTON_ON else TypeEvent.LOCATION_BUTTON_OFF, details = onOrOff, date = AssignmentHelper.retrieveDateFORMATTER()))

    }
}