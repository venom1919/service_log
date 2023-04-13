package com.service_log.receiver

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

class BatteryChangedReceiver: BroadcastReceiver() {

    private lateinit var dao: TripRepository
    private var strength = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(p0: Context?, p1: Intent?) {

        var level = AssignmentHelper.retrieveReceiverINFOByBattery(p0!!).toInt()

        if (level != strength){

            dao = TripRepository(p0)
            dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(p0), type = TypeEvent.BATTERY_CHANGE, details = level.toString(), date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))

        }
    }
}