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
import com.service_log.enums.TypeEvent
import com.service_log.model.Trip
import com.service_log.repository.TripRepository
import com.service_log.service.static.AssignmentHelper
import java.text.DecimalFormat


@RequiresApi(Build.VERSION_CODES.O)
class TrackTrace(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

//    private var fusedLocationClient2: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest
    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback
    private lateinit var imei: String


    ///////////////////2
    private var dao: TripRepository
    lateinit var mLocationClient: GoogleApiClient
    private val priority = PRIORITY_HIGH_ACCURACY

    var mLocationRequest = LocationRequest()
    var context: Context
    var startLatitude = 0.0
    var startLongitude = 0.0

    private var locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    init {

        this.context = context

        dao = TripRepository(context)
        imei = AssignmentHelper.retrieveReceiverInfoByIMEI(context)

        if (locationIsActiveCurrent()) {

            Log.i("locationall", "")

            initial(context)
            getLocationUpdates()
            startLocationUpdates(context)

//        fusedLocationClient2 = FusedLocationProviderClient(context)


            mFusedLocationClient = FusedLocationProviderClient(context)
            getLocationOldMethod()

//        var isGPSEnabled = locationManager
//            .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        // getting network status
//        var isNetworkEnabled = locationManager
//            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        Log.i("nnn1", isNetworkEnabled.toString() +  " " + isGPSEnabled.toString())
        }
    }

    private fun locationIsActiveCurrent() : Boolean{

        val isGPSEnabled = locationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER);

        val isNetworkEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.i("falsesl____00000", isGPSEnabled.toString() + " "+ isGPSEnabled)


        if (!isGPSEnabled || !isNetworkEnabled){

            Log.i("falsesl", isGPSEnabled.toString() + " "+ isGPSEnabled)
            dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION_BUTTON_OFF, details = "", date = AssignmentHelper.retrieveDateFORMATTER()))
            return false
        }

        return true

    }

    private fun getLocationUpdates()
    {

        if (!locationIsActiveCurrent()){
            return
        }

//        fusedLocationClient2 = LocationServices.getFusedLocationProviderClient(context!!)
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
//        fusedLocationClient2.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            null
//        )
    }

    // stop location updates
//    private fun stopLocationUpdates() {
//        fusedLocationClient2.removeLocationUpdates(locationCallback)
//    }


    private fun initial(context: Context){
        if (!locationIsActiveCurrent()){
            return
        }
        mLocationClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 0
        mLocationRequest.priority = priority
        mLocationClient.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("faileddd", "")
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {

        Log.i("Conecteded", "")
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

    override fun onLocationChanged(p0: Location?) {

        var startLat = p0!!.latitude
        var startLong = p0.longitude

//        calculateDistance(p0.longitude, p0.latitude)
        Log.i("tatatacx", p0.getAccuracy().toString() + " " + p0.latitude.toString() + " " + p0.longitude.toString())

        if (p0.getAccuracy() > 100){
            getLocationOldMethod()
        }

        Log.i("datata",AssignmentHelper.retrieveDateFORMATTER())
//        dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION, details = p0.latitude.toString() + " " + p0.longitude.toString(), date = AssignmentHelper.retrieveDateFORMATTER()))

        val data = dao.getAllTrip()
        data.forEach {
            Log.i("detete", it.date)
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocationOldMethod(): Unit? {

        Log.i("rtyreyo", "")
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener: android.location.LocationListener = object : android.location.LocationListener {

            override fun onLocationChanged(location: Location) {
                dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION, details = location.latitude.toString() + " " + location.longitude.toString(), date = AssignmentHelper.retrieveDateFORMATTER()))
            }

            override fun onFlushComplete(requestCode: Int) {
                super.onFlushComplete(requestCode)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }

            override fun onProviderDisabled(provider: String) {

            }
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

        return null
    }

    fun correctFilterDistance(): Boolean{
        return false
    }

    private fun calculateDistance(endLat:Double, endLon:Double ){

        val radius = 6371

        val lat1: Double = startLatitude
        val lat2: Double = endLat
        val lon1: Double = startLongitude
        val lon2: Double = endLon
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))

        Log.i("Radius_alue", "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec + " " + endLat + " " + endLon + "df  " + radius * c)

        startLatitude = endLat
        startLongitude = endLon

//        val R = 6371; // km
//        val dLat = (lat-lat)
//        val dLon = (lon-lon)
//        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.cos(lat) * Math.cos(lat) *
//                Math.sin(dLon/2) * Math.sin(dLon/2)
//        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
//        val d = R * c
//
//
//
//        latitude = lat2
//        longitude = lon2

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

private fun LocationManager.requestLocationUpdates(
    gpsProvider: String,
    i: Int,
    i1: Int,
    locationListener: LocationListener
) {

    Log.i("gpsDatc", i.toString())
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


//        rt(status.satelliteCount, status)

    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun rt(fl: Int, status: GnssStatus){
//
//        for (i in 0 until fl){
//            if (status.getCn0DbHz(i) > 0){
//                Log.i("scxШУм", status.getCn0DbHz(i).toString() + " " +  status.getSvid(i))
//
//            }
//            Log.i("lktr", status.getAzimuthDegrees(i).toString())
//        }
//
//    }


}

