package com.service_log.service.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.GnssStatus
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
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.service_log.repository.TripRepository


@RequiresApi(Build.VERSION_CODES.S)
class TrackTrace(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var fusedLocationClient2: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    ///////////////////2
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
//        initial(context)
        getLocationUpdates()
        startLocationUpdates(context)
        //////

        fusedLocationClient2 = FusedLocationProviderClient(context)

        dao = TripRepository(context)
        mFusedLocationClient = FusedLocationProviderClient(context)

    }

    private fun getLocationUpdates()
    {

        fusedLocationClient2 = LocationServices.getFusedLocationProviderClient(context!!)
        locationRequest = LocationRequest()
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
//        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {

                    Log.i("lastposss", locationResult.lastLocation.longitude.toString() + " " + locationResult.lastLocation.latitude.toString() )

                    val location =
                        locationResult.lastLocation
                 }


            }
        }
    }

    //start location updates
    private fun startLocationUpdates(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient2.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient2.removeLocationUpdates(locationCallback)
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


        Log.i("Conecteded" , "")
        locationManager.registerGnssStatusCallback(T())

//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1F, (LocationListener {
//        }){
//
//        })

        getNetworkStrength()
        getLocation()

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun rt(fl: Int, status: GnssStatus){

        for (i in 0 until fl){
            if (status.getCn0DbHz(i) > 0){
                Log.i("scxШУм", status.getCn0DbHz(i).toString() + " " +  status.getSvid(i))

            }
            Log.i("lktr", status.getAzimuthDegrees(i).toString())
        }

    }



}

