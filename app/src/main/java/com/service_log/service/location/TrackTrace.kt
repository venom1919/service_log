package com.service_log.service.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.GnssStatus
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.O)
class TrackTrace(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

//    private var fusedLocationClient2: FusedLocationProviderClient

    val arr = DoubleArray(2)

    var lstLongitude = 0.0
    var lstLatitude = 0.0

    var checkLongitude = 0.0
    var checkLatitude = 0.0

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var imei: String

//    var permission = true

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


    private val mStatusCallback: GnssStatus.Callback = object : GnssStatus.Callback() {
        override fun onStarted() {}
        override fun onStopped() {}
        override fun onFirstFix(ttffMillis: Int) {}
        override fun onSatelliteStatusChanged(status: GnssStatus) {
        }
    }

    init {

//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//           permission = false
//        }


        this.context = context

        locationManager.registerGnssStatusCallback(mStatusCallback)
        dao = TripRepository(context)
        imei = AssignmentHelper.retrieveReceiverInfoByIMEI(context)

        if (locationIsActiveCurrent()) {

//            initial(context)
//            getLocationUpdates()
//            startLocationUpdates(context)
            getlocation2(context)
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

    @SuppressLint("ServiceCast", "MissingPermission")
    fun isOnline(context: Context, signal :String): String {

        var gnssStatus: GnssStatus
        var detalization = ""

          //gnssStatus.satelliteCount()

        ///2 ______WAN
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return ""
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return "true"
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return "true"
                }
            }
        }
        return "false"
    }

    private fun locationIsActiveCurrent() : Boolean{
//
//        if (!permission){
//            return false
//        }

        val isGPSEnabled = locationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER);

        val isNetworkEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled || !isNetworkEnabled){

            dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION_BUTTON_OFF, details = "", date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))
                return false
        }
        return true
    }

    private fun getLocationUpdates() {
        if (!locationIsActiveCurrent()){
            return
        }

//      fusedLocationClient2 = LocationServices.getFusedLocationProviderClient(context!!)
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

//        if (!permission){
//            return
//        }

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

//        if (!permission){
//            return
//        }

//        locationManager.registerGnssStatusCallback(mStatusCallback)

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
        ) {
            dao.insertTrip(Trip(imei = AssignmentHelper.retrieveReceiverInfoByIMEI(context), type = TypeEvent.PERMISSION_OFF, details = "app settings don`nt have permission ", date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))
            return
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
            mLocationClient,
            mLocationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onLocationChanged(p0: Location?) {

        var startLat = p0!!.latitude
        var startLong = p0.longitude

//        calculateDistance(p0.longitude, p0.latitude)

        if (p0.getAccuracy() > 100){
            getLocationOldMethod()
        }

//        dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION, details = p0.latitude.toString() + " " + p0.longitude.toString(), date = AssignmentHelper.retrieveDateFORMATTER()))

        val data = dao.getAllTrip()
        data.forEach {
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    fun getlocation2(context: Context){

//        val pm = context.getSystemService(POWER_SERVICE) as PowerManager?
//
//        val cpuWakeLock = pm!!.newWakeLock(
//            PowerManager.PARTIAL_WAKE_LOCK,
//            "gps_service_s"
//        )
//        cpuWakeLock.acquire()
//
//        // Release in onDestroy of your service
//        if (cpuWakeLock.isHeld()) cpuWakeLock.release()
        var latitude = 0.0
        var longitude = 0.0

        val locationListenerTSD: android.location.LocationListener =
            object : android.location.LocationListener {
                override fun onLocationChanged(location: Location) {

//                    val acc = location.accuracy
                    latitude = location.latitude
                    longitude = location.longitude

                    if (latitude == checkLatitude && longitude == checkLongitude) {
                        return
                    }

//                    Log.i("firsat", latitude.toString() + " " + checkLatitude + " " + longitude.toString() + " " + longitude)
                    dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION, details = "$latitude $longitude", date = AssignmentHelper.retrieveDateFORMATTER(), info = "old_method"))
                    checkLongitude = longitude
                    checkLatitude = latitude
                    location.reset()

                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                    Log.i("provider_isEnY", provider)
                }

                override fun onProviderEnabled(provider: String) {
                    Log.i("provider_isEnc", provider)
                }

                override fun onProviderDisabled(provider: String) {
                    Log.i("provider_isEnV", provider)
                }

                override fun onLocationChanged(locations: MutableList<Location>) {
                    super.onLocationChanged(locations)
                }
            }

        val locationManagerT = context.getSystemService(LOCATION_SERVICE) as LocationManager

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

        val bestProvider = locationManagerT.getBestProvider(Criteria(), true)

        locationManagerT.requestLocationUpdates(
            bestProvider!!,
            0,
            1000f,
            locationListenerTSD
        )

        locationManagerT.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            100,
            100f,
            locationListenerTSD
        )

        ////////2
        val mlocListener: android.location.LocationListener =
            object : android.location.LocationListener {
                override fun onLocationChanged(location: Location) {

                    val acc = location.accuracy
                    latitude = location.latitude
                    longitude = location.longitude

                    if (acc > 100){
                        return
                    }

                    if (lstLongitude == 0.0 || lstLatitude == 0.0){
                        lstLatitude = latitude
                        lstLongitude = longitude
                    }

                    var lat1: Double = arr.get(0)
                    val lon1: Double = arr.get(1)
                    var lat2: Double = latitude
                    val lon2: Double = longitude

                    val R = 6371.0 // km

                    val dLat = (lat2 - lstLongitude) * Math.PI / 180
                    val dLon = (lon2 - lon1) * Math.PI / 180
                    lstLongitude = lat1 * Math.PI / 180
                    lat2 = lat2 * Math.PI / 180

                    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2)
                    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
                    val d = R * c * 1000

                    if (latitude == checkLatitude && longitude == checkLongitude){
                        return
                    }

//                    Log.i("first222", latitude.toString() + " " + checkLatitude + " " + longitude.toString() + " " + longitude)

                    dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION, details = "$latitude $longitude", date = AssignmentHelper.retrieveDateFORMATTER(), info = "isOnline(context, acc.toString())"))
                    checkLatitude = latitude
                    checkLongitude = longitude
                    location.reset()

                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                    Log.i("provider_isEnY", provider)
                }

                override fun onProviderEnabled(provider: String) {
                    Log.i("provider_isEnc", provider)
                }

                override fun onProviderDisabled(provider: String) {
                    Log.i("provider_isEnV", provider)
                }

                override fun onLocationChanged(locations: MutableList<Location>) {
                    super.onLocationChanged(locations)
                }
            }

        val locationManagerT_2 = context.getSystemService(LOCATION_SERVICE) as LocationManager

        locationManagerT_2.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            mlocListener
        )
    }

    @SuppressLint("MissingPermission")
    fun getLocationOldMethod(): Unit? {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener: android.location.LocationListener = object : android.location.LocationListener {

            override fun onLocationChanged(location: Location) {

                if (location.latitude == checkLatitude && location.longitude == checkLongitude){
                    return
                }

//                Log.i("second", location.latitude.toString() + " " + checkLatitude + " " + location.longitude.toString() + " " + checkLongitude)

                dao.insertTrip(Trip(imei = imei, type = TypeEvent.LOCATION, details = location.latitude.toString() + " " + location.longitude.toString(), date = AssignmentHelper.retrieveDateFORMATTER(), info = ""))
                checkLatitude = location.latitude
                checkLongitude = location.longitude

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }

            override fun onProviderEnabled(provider: String) {
                return
//                super.onProviderEnabled(provider)
            }

            override fun onProviderDisabled(provider: String) {

            }
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

        return null
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

//        Log.i("Radius_alue", "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec + " " + endLat + " " + endLon + "df  " + radius * c)

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

//    @SuppressLint("MissingPermission", "SetTextI18n")
//    private fun getLocation(context: Context) {
//        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
//            var location: Location? = task.result
//        }
//    }


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
        }
    }

//private fun LocationManager.requestLocationUpdates(
//    gpsProvider: String,
//    i: Int,
//    i1: Int,
//    locationListener: LocationListener
//) {
//
//    Log.i("gpsDatc", i.toString())
//}

//@RequiresApi(Build.VERSION_CODES.N)
//open class T : GnssStatus.Callback() {
//
//    override fun onStarted() {
//
//        super.onStarted()
//        Log.i("dxc", "")
//
//    }
//
//    override fun onStopped() {
//        super.onStopped()
//    }
//
//    override fun onFirstFix(ttffMillis: Int) {
//        super.onFirstFix(ttffMillis)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.R)
//    override fun onSatelliteStatusChanged(status: GnssStatus) {
//        super.onSatelliteStatusChanged(status)
//
//
////        rt(status.satelliteCount, status)
//
//    }
//
////    @RequiresApi(Build.VERSION_CODES.O)
////    fun rt(fl: Int, status: GnssStatus){
////
////        for (i in 0 until fl){
////            if (status.getCn0DbHz(i) > 0){
////                Log.i("scxШУм", status.getCn0DbHz(i).toString() + " " +  status.getSvid(i))
////
////            }
////            Log.i("lktr", status.getAzimuthDegrees(i).toString())
////        }
////
////    }
//
//
//}

