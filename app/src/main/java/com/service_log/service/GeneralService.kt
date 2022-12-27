package com.service_log.service

import android.R
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.service_log.receiver.AlarmReceiver
import com.service_log.service.location.TrackTrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GeneralService : Service(), GoogleApiClient.OnConnectionFailedListener  {

    lateinit var trackTrace:TrackTrace

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {

        val builder: Notification.Builder = Notification.Builder(this)
            .setSmallIcon(R.drawable.star_on)
        val notification: Notification = if (Build.VERSION.SDK_INT < 16) builder.getNotification() else builder.build()

        startForeground(777, notification)

        trackTrace = TrackTrace(this)
        writeData()

    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }


    /////Send data 1c
    @SuppressLint("UnspecifiedImmutableFlag")
    fun writeData(){

        val receiver = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            100000,
            pendingIntent
        )

        runBlocking {
                launch(Dispatchers.Default) {
            }
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(Runnable {
//            getLocation(this)
            Log.i("Jobss", "yes")
        }, 0, 3, TimeUnit.SECONDS)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

}