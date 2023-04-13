package com.service_log.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.service_log.R
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.receiver.BatteryChangedReceiver
import com.service_log.repository.TripRepository
import com.service_log.service.GeneralService
import com.service_log.service.static.AssignmentHelper


open class MainActivity: AppCompatActivity(){

    private lateinit var dao: TripRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

//        dao = TripRepository(this)
//        dao.updateData()

        val p = packageManager
        val componentName = ComponentName(
            this,
            MainActivity::class.java
        )
        p.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        dao = TripRepository(this)
        dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(this), type = TypeEvent.APP_ON, details = "app is on", date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))

        registerReceiver(BatteryChangedReceiver(), IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        ////1+++++++++
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val i = Intent(this, GeneralService::class.java)
        startService(i)
        ////1--------

        val ctx: Context = this // or you can replace **'this'** with your **ActivityName.this**

        try {
            val i = ctx.packageManager.getLaunchIntentForPackage("com.treedo.taburetka.tsd")
            ctx.startActivity(i)
        } catch (_: PackageManager.NameNotFoundException) {

        }

        //////2++++++++++++
//        val receiver = Intent(this, LocationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, 0)
//
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        fusedLocationClient.requestLocationUpdates(LocationRequest.create(), pendingIntent).addOnSuccessListener {
///            lis -> lis.
//        }
        //////2------------

    }
}