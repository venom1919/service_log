package com.service_log.model

import android.app.Activity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class Location( val pcode: Int,
                val locality: String,
                val state: String,
                val comments: String,
                val category: String,
                val longitude: Double,
                val latitude: Double,
                activity: Activity,
                locationListener: com.service_log.service.location.LocationListener) {


    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null
//    private const val LOCATION_REQUEST_INTERVAL: Long = 5000




//    private fun createLocationRequest() {
//        mLocationRequest = LocationRequest.create()
//        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        mLocationRequest!!.setInterval(LOCATION_REQUEST_INTERVAL).fastestInterval =
//            LOCATION_REQUEST_INTERVAL
//        requestLocationUpdate()
//    }

//    private fun requestLocationUpdate() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED ) {
//
//            Log.e("permission", "denied");
//
//
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//
//        mFusedLocationProviderClient!!.requestLocationUpdates(
//            mLocationRequest,
//            mLocationCallback,
//            Looper.myLooper()
//        )
//    }



}
//    private var fusedLocationClient: FusedLocationProviderClient?=null
//    private var locationRequest: LocationRequest?=null
//    private var callbabck: LocationCallback?=null
//
//
//    fun onCreate(){
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//
//        inicializeLocationRequest()
//        callbabck = object : LocationCallback() {
//
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//                val lat  = locationResult.lastLocation.latitude
//                val lng = locationResult.lastLocation.longitude
//
//
//            }
//        }
//
//
//    }
//
//
//    init {
//        fusedLocationClient= FusedLocationProviderClient(activity.applicationContext)
//
//        inicializeLocationRequest()
//        callbabck=object: LocationCallback(){
//            override fun onLocationResult(p0: LocationResult?) {
//                super.onLocationResult(p0)
//
//                locationListener.locationResponse(p0!!)
//
//
//            }
//        }
//
//
////        fusedLocationClient.(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
////            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
////
////            override fun isCancellationRequested() = false
////        })
////            .addOnSuccessListener { location: Location? ->
////                if (location == null)
////                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
////                else {
////                    val lat = location.latitude
////                    val lon = location.longitude
////                }
////
////            }
//
//
//
//
//
//    }
//
//    private fun requestLocationUpdate() {
//
//           return
//        }
//
//
//        private fun inicializeLocationRequest() {
//        locationRequest= LocationRequest()
//        locationRequest?.interval=50000
//        locationRequest?.fastestInterval=5000
//        locationRequest?.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
//        requestLocationUpdate()
//    }
//
//
//    fun accummulate() {
//
//    }



