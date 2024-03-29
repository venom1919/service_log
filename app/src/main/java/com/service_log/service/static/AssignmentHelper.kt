package com.service_log.service.static

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import com.service_log.enums.TypeEvent
import com.service_log.enums.TypeEvent.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@Suppress("UNUSED_EXPRESSION")
class AssignmentHelper {

    companion object {

        @SuppressLint("ServiceCast")
        fun retrieveReceiverInfoByIMEI(context: Context): String{
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as
                    TelephonyManager
            return telephonyManager.deviceId
        }

        @SuppressLint("ServiceCast")
        fun retrieveReceiverINFOByLocationButton(context: Context): Boolean{
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as
                    LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        @SuppressLint("ServiceCast")
        fun retrieveReceiverINFOByAirplaneStatus(context: Context): String{

            val res = Settings.Global.getString(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON).toInt()
            return if (res > 0)
                "On"
            else
                "Off"
        }

        @SuppressLint("ServiceCast")
        fun retrieveReceiverINFOByBattery(context: Context): String {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as
                    BatteryManager
            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).toString()
        }

        fun retrieveDETAILS(typeEvent: TypeEvent): String{

            when(typeEvent){
                REBOOT -> return "phone has been reboot"
                LOCATION -> return "Location is"
                AIRPLANE_MODE -> return "user power AIRPLANE_MODE"
                BATTERY_CHANGE -> return "balance battery"
                CHANGE_STATE_1C -> return "1c has been closed"
                LOCATION_BUTTON_OFF -> return "user turned off location.GPS don't work"
                LOCATION_BUTTON_ON -> return "user turned on location.GPS work"
                POWER_ON -> return "phone has been turned on!!!!"
                APP_ON -> return "app has been enabled!!!!"
                PERMISSION_OFF -> TODO()
            }

            return ""
        }

//        @SuppressLint("ServiceCast")
//        fun retrieveDataAbout1c(context: Context): kotlin.collections.HashMap<String, Boolean> {
//            val hashMap:HashMap<String, Boolean> = HashMap<String, Boolean>()
//            hashMap.put("", true)
//            return hashMap
//        }

        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O)
        fun retrieveDateFORMATTER(): String {
//            return SimpleDateFormat("").format(Date())
            return SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date())
        }
    }
}



