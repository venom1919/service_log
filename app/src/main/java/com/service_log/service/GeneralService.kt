package com.service_log.service

//import android.support.v4.app.ActivityCompat
import android.R
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.service_log.api.ApiService
import com.service_log.model.Location
import com.service_log.receiver.AlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GeneralService : Service(), GoogleApiClient.OnConnectionFailedListener  {

    lateinit var retrofitInstance: ApiService
    var location: Location?=null
//    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient

    override fun onCreate() {
        val builder: Notification.Builder = Notification.Builder(this)
            .setSmallIcon(R.drawable.star_on)
        val notification: Notification = if (Build.VERSION.SDK_INT < 16) builder.getNotification() else builder.build()
        startForeground(777, notification)
        Log.i("servicess", "www")
        writeData()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


//        val client = GoogleApiClient.Builder(this)
//            .enableAutoManage(this, this)
//            .addApi(LocationServices.API)
//            .build()
//        client.connect()

//        fusedLocationClient.
//        if (androidx.core.app.ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && androidx.core.app.ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
//                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
//                override fun isCancellationRequested() = false
//            }).addOnSuccessListener { loc: android.location.Location? ->  if (location == null)
//                Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
//            else {
//                    val lat = loc?.longitude
//                    val lon = loc?.longitude
//            }
//
//            }
//
//            return
//        }

    }

    fun exApiGoogle(apiClient: GoogleApiClient){

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
    @SuppressLint("UnspecifiedImmutableFlag")
    fun writeData(){

        retrofitInstance = ApiService()

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
                Log.i("${Thread.currentThread()} has run.", "strick")
            }
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(Runnable {
            Log.i("Jobss", "yes")
        }, 0, 3, TimeUnit.SECONDS)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}