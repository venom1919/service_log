package com.service_log.service

//import android.support.v4.app.ActivityCompat
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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.service_log.api.ApiService
import com.service_log.db.TripDB
import com.service_log.model.Location
import com.service_log.receiver.AlarmReceiver
import com.service_log.service.location.TrackTrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.DateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.log

class GeneralService : Service(), GoogleApiClient.OnConnectionFailedListener  {

    lateinit var retrofitInstance: ApiService

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mLocationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient
    lateinit var trackTrace:TrackTrace

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {

        val builder: Notification.Builder = Notification.Builder(this)
            .setSmallIcon(R.drawable.star_on)
        val notification: Notification = if (Build.VERSION.SDK_INT < 16) builder.getNotification() else builder.build()

        startForeground(777, notification)

        Log.i("servicess", "www")

        mFusedLocationClient = FusedLocationProviderClient(this)

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 0
        mLocationRequest!!.fastestInterval = 0
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.i("ssccs", locationResult.lastLocation.longitude.toString()  + " " + locationResult.lastLocation.toString())
            }
        }

        trackTrace = TrackTrace(this)
        writeData()

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
            getLocation(this)
            Log.i("Jobss", "yes")
        }, 0, 3, TimeUnit.SECONDS)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation(context: Context) {

        mFusedLocationClient.lastLocation.addOnCompleteListener {task ->

            var location: android.location.Location? = task.result

        }
    }

}