package com.service_log.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.service_log.service.GeneralService

class InstallReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        try {

            val i = Intent(p0, GeneralService::class.java)
            p0?.startService(i)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}