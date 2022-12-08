package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.service_log.activity.MainActivity
import com.service_log.service.GeneralService

class RebootReceiver : BroadcastReceiver() {

//    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i("jjj", "sdsds")
        if (p0 != null){
            val i = Intent(p0, GeneralService::class.java)
            p0.startService(i)
        }
    }
}