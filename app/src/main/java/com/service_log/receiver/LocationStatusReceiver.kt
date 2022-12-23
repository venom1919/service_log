package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.service_log.service.static.AssignmentHelper
import kotlin.math.log

class LocationStatusReceiver : BroadcastReceiver() {

    @SuppressLint("ServiceCast")
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i("sxcxcxc",  AssignmentHelper.retrieveReceiverINFOByLocationButton(p0!!).toString())

    }
}