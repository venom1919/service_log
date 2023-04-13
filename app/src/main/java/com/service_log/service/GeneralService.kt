package com.service_log.service


import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.*
import com.service_log.receiver.AlarmReceiver
import com.service_log.service.location.TrackTrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class GeneralService : Service() {

    lateinit var trackTrace: TrackTrace

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate() {

//        val p = packageManager
//        val componentName = ComponentName(
//           this,
//           com.service_log.activity.MainActivity::class.java
//        ) // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
//
//        p.setComponentEnabledSetting(
//            componentName,
//            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//            PackageManager.DONT_KILL_APP
//        )

        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val startTime = calendar.timeInMillis

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

    @RequiresApi(Build.VERSION_CODES.M)
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
            launch(Dispatchers.Default) {}
        }

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            status1C()
        }, 0, 3, TimeUnit.SECONDS)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun status1C(){

        try {
            val process: java.lang.Process? = Runtime.getRuntime().exec("/system/bin/ps")
            val reader = InputStreamReader(process!!.getInputStream())
            val bufferedReader = BufferedReader(reader)
            var numRead: Int
            val buffer = CharArray(5000)
            val commandOutput = StringBuffer()
            while (bufferedReader.read(buffer).also { numRead = it } > 0) {
                commandOutput.append(buffer, 0, numRead)
            }
            bufferedReader.close()
            process.waitFor()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }




//        val pr = Process.getUidForName("com.treedo.taburetka.tsd")
//
//        Log.i("sxw23", pr.toString())
//
//
//
//        var currentApp: String? = null
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//
//            Log.i("wweeer","")
//            val usm = this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
//            val time = System.currentTimeMillis()
//            val appList: List<UsageStats>?
//            appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
//
//            Log.i("xx1", usm.isAppInactive("com.treedo.taburetka.tsd").toString())
//
//            if (appList != null && appList.isNotEmpty()) {
//                val sortedMap = TreeMap<Long, UsageStats>()
//                for (usageStats in appList) {
//                    sortedMap.put(usageStats.lastTimeUsed, usageStats)
//                }
//                currentApp = sortedMap.takeIf { it.isNotEmpty() }?.lastEntry()?.value?.packageName
//
//                Log.i("ssxcx", currentApp.toString())
//
//            }
//
//        } else {
//            Log.i("wwzxzxzxr","")
//            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//            @Suppress("DEPRECATION") //The deprecated method is used for devices running an API lower than LOLLIPOP
//            currentApp = am.getRunningTasks(1)[0].topActivity?.packageName
//            Log.i("CXC", currentApp.toString())
//        }

    }

}

@SuppressLint("MissingPermission")
private fun getLastLocation(mFusedLocationClient : FusedLocationProviderClient, context: Context) {

    mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
        var location: Location? = task.result

        if (location == null) {
            Log.i("locasss" , "nulllll")
            requestNewLocationData(mFusedLocationClient ,context)
        } else {
            Log.i("bvc", location.latitude.toString())
        }
    }
}


@SuppressLint("MissingPermission")
private fun requestNewLocationData(mFusedLocationClient : FusedLocationProviderClient, context: Context) {
    var mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = 0
    mLocationRequest.fastestInterval = 0
    mLocationRequest.numUpdates = 1

   var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    mFusedLocationClient!!.requestLocationUpdates(
        mLocationRequest, mLocationCallback,
        Looper.myLooper()
    )
}

private val mLocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        var mLastLocation: Location = locationResult.lastLocation
        Log.i("lastlocationn", mLastLocation.longitude.toString())
    }
}

private fun isLocationEnabled(context: Context): Boolean {
    var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE)  as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}



