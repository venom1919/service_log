package com.service_log.service.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.GpsSatellite
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.telephony.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.service_log.repository.TripRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.S)
class TrackTrace(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var dao: TripRepository
    lateinit var mLocationClient: GoogleApiClient
    private val priority = PRIORITY_HIGH_ACCURACY

    var mLocationRequest = LocationRequest()
    var context: Context
    var latitude = 0.0
    var longitude = 0.0

    private var locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    private var mFusedLocationClient: FusedLocationProviderClient

    init {
        this.context = context
        initial(context)
        dao = TripRepository(context)
        mFusedLocationClient = FusedLocationProviderClient(context)

    }

    private fun initial(context: Context){
        mLocationClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 0
        mLocationRequest.priority = priority
        mLocationClient.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("faileddd", "")
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {

        val status = locationManager.getGpsStatus(null)
        val satellites: Iterable<GpsSatellite> = status!!.satellites

        val satI = satellites.iterator()

        val maxSatellites: Int = status.getMaxSatellites()

        for (item in status.satellites) {
           Log.i("[[[[[[", item.snr.toString())
        }
//        status.apply { gpsStatus -> Log.i("xx3c", gpsStatus.toString())}
//
//        locationManager.registerGnssStatusCallback(T())
//        var b = locationManager.getGpsStatus(null)!!.satellites.first {}
//        Log.i("sc3w", b.toString())
//        locationManager.getGpsStatus(null)!!.getTimeToFirstFix()

        getNetworkStrength()
        getLocation()
        mFusedLocationClient.lastLocation.addOnSuccessListener {
            getLocation(context)
        }

        Log.i("connceter", "")

        CoroutineScope(Dispatchers.IO).launch {

            Log.i("coroutiness","")
            val p = dao.getAllTrip()
            for (trip in p){
                Log.i("ssssss2222", trip.id.toString())
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { return }

        LocationServices.FusedLocationApi.requestLocationUpdates(
            mLocationClient,
            mLocationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i("xcxqe23", "xzx")
    }

    override fun onLocationChanged(p0: android.location.Location?) {


        calculateDistance(latitude, longitude, p0!!.longitude, p0.latitude)

        Log.i("tatatacx", p0?.getAccuracy().toString() + " " + p0?.latitude.toString() + " " + p0?.longitude.toString())

        val data = dao.getAllTrip()
        data.forEach {
            Log.i("detete", it.details)
        }
    }

    fun correctFilterDistance(): Boolean{
        return false
    }

    private fun calculateDistance(lat:Double, lon:Double, lat2:Double, lon2:Double ){

        val R = 6371; // km
        val dLat = (lat-lat)
        val dLon = (lon-lon)
        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat) * Math.cos(lat) *
                Math.sin(dLon/2) * Math.sin(dLon/2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        val d = R * c

        Log.i("sssxc", d.toString())

        latitude = lat2
        longitude = lon2

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation(context: Context) {
        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            var location: Location? = task.result
            Log.i("listnern", location?.latitude.toString() + " " + location?.longitude.toString())
        }
    }


    fun getRegisteredCellInfo(cellInfos: MutableList<CellInfo>): ArrayList<CellInfo> {
        val registeredCellInfos = ArrayList<CellInfo>()
        if (cellInfos.isNotEmpty()) {
            for (i in cellInfos.indices) {
                if (cellInfos[i].isRegistered) {
                    registeredCellInfos.add(cellInfos[i])
                }
            }
        }
        return registeredCellInfos
    }

    fun getNetworkStrength() {

        var strength1 = -1
        var strength2 = -1

        val manager =  context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            if (telephonyManager.allCellInfo != null) {

                val allCellinfo = telephonyManager.allCellInfo
                val activeSubscriptionInfoList = manager.activeSubscriptionInfoList

                val regCellInfo= getRegisteredCellInfo(allCellinfo)


                activeSubscriptionInfoList.forEachIndexed { Subindex, subs ->

                    if (activeSubscriptionInfoList.size >= 2) {

                        if (regCellInfo.size >= 2) {

                            if (subs.simSlotIndex == 0) {

                                if (subs.carrierName != "No service") {


                                    strength1 = when (val info1 = regCellInfo[0]) {
                                        is CellInfoLte -> info1.cellSignalStrength.dbm
                                        is CellInfoGsm -> info1.cellSignalStrength.dbm
                                        is CellInfoCdma -> info1.cellSignalStrength.dbm
                                        is CellInfoWcdma -> info1.cellSignalStrength.dbm
                                        else -> 0
                                    }


                                } else {

                                    strength1 = -1
                                }

                            } else if (subs.simSlotIndex == 1) {

                                if (subs.carrierName != "No service") {

                                    strength2 = when (val info2 = regCellInfo[1]) {
                                        is CellInfoLte -> info2.cellSignalStrength.dbm
                                        is CellInfoGsm -> info2.cellSignalStrength.dbm
                                        is CellInfoCdma -> info2.cellSignalStrength.dbm
                                        is CellInfoWcdma -> info2.cellSignalStrength.dbm
                                        else -> 0
                                    }

                                    Log.i("sim2", subs.carrierName.toString())
                                } else {

                                    strength2 = -1
                                }

                            }

                        }
                    }else if(activeSubscriptionInfoList.size == 1)
                    {

                        if(regCellInfo.size >= 1) {

                            if (subs.simSlotIndex == 0) {

                                if (subs.carrierName != "No service") {


                                    strength1 = when (val info1 = regCellInfo[0]) {
                                        is CellInfoLte -> info1.cellSignalStrength.level
                                        is CellInfoGsm -> info1.cellSignalStrength.level
                                        is CellInfoCdma -> info1.cellSignalStrength.level
                                        is CellInfoWcdma -> info1.cellSignalStrength.level
                                        else -> 0
                                    }

                                    Log.i("subsccc",  subs.toString())

//                                    Timber.i("sim1   ${subs.carrierName}  ${subs.mnc}  $strength1")
                                } else {

                                    strength1 = -1
                                }

                            }
                        }

                        strength2 = -2

                    }
                }

            }
        }

        Log.i("final strenght ",  strength1.toString())

    }

    }

@RequiresApi(Build.VERSION_CODES.N)
open class T : GnssStatus.Callback() {

    override fun onStarted() {

        super.onStarted()
        Log.i("dxc", "")

    }

    override fun onStopped() {
        super.onStopped()
    }

    override fun onFirstFix(ttffMillis: Int) {
        super.onFirstFix(ttffMillis)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onSatelliteStatusChanged(status: GnssStatus) {
        super.onSatelliteStatusChanged(status)

        Log.i("xxcw26", status.satelliteCount.toString())

        rt(status.satelliteCount, status)
    }

    fun rt(fl: Int, status: GnssStatus){

        for (i in 0 until fl){

            Log.i("scx", status.getAzimuthDegrees(i).toString())
            Log.i("scx2", status.getConstellationType(i).toString())

        }

    }

}

