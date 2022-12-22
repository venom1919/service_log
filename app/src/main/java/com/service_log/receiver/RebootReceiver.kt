package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.repository.TripRepository
import com.service_log.service.GeneralService

class RebootReceiver : BroadcastReceiver() {

    private lateinit var dao: TripRepository

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null){
            dao = TripRepository(p0)
            dao.insertTrip(Trip(imei = "", type = TypeEvent.REBOOT, details = "phone was reboot", date = ""))
            val i = Intent(p0, GeneralService::class.java)
            p0.startService(i)
        }
    }
}