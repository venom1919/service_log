package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.repository.TripRepository
import com.service_log.service.location.TrackTrace
import com.service_log.service.static.AssignmentHelper
import kotlin.concurrent.thread

class LocationStatusReceiver : BroadcastReceiver() {

    private lateinit var dao: TripRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(p0: Context?, p1: Intent?) {

        val locationStatus = AssignmentHelper.retrieveReceiverINFOByLocationButton(p0!!)

        Log.i("receiver_Location_on", locationStatus.toString())

        val onOrOff: String = if (locationStatus){
            TrackTrace(p0)
            AssignmentHelper.retrieveDETAILS(TypeEvent.LOCATION_BUTTON_ON)
        }else{
            AssignmentHelper.retrieveDETAILS(TypeEvent.LOCATION_BUTTON_OFF)
        }

        dao = TripRepository(p0)
        dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(p0), type = if (locationStatus) TypeEvent.LOCATION_BUTTON_ON else TypeEvent.LOCATION_BUTTON_OFF, details = onOrOff, date = AssignmentHelper.retrieveDateFORMATTER()))

    }
}