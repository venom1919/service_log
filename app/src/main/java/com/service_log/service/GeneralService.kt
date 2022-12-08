package com.service_log.service

import android.R
import android.app.AlarmManager
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class GeneralService : Service() {

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun onCreate() {
        val builder: Notification.Builder = Notification.Builder(this)
            .setSmallIcon(R.drawable.star_on)
        val notification: Notification = if (Build.VERSION.SDK_INT < 16) builder.getNotification() else builder.build()
        startForeground(777, notification)
        Log.i("servicess", "www")
        writeData()
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.i("ncn", "lllll")
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//     return super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.i("remove tsk", "yes")
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        Log.i("destroy", "yes")
        super.onDestroy()
    }


    fun locationDataChange(){

    }

    /////Send data 1c
    fun writeData(){

        val manager = getSystemService(ALARM_SERVICE) as AlarmManager

        runBlocking {
                launch(Dispatchers.Default) {
                Log.i("${Thread.currentThread()} has run.", "starick")
            }
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(Runnable {
            Log.i("Jobss", "yes")
        }, 0, 3, TimeUnit.SECONDS)
    }

}