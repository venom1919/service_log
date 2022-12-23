package com.service_log.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.service_log.service.static.AssignmentHelper

class AirplaneStatusReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i("airpalne c-mode",
            AssignmentHelper.retrieveReceiverINFOByAirplaneStatus(p0!!).toString()
        )
    }
}