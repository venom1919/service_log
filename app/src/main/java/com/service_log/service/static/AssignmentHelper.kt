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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AssignmentHelper {

    companion object {

        @SuppressLint("ServiceCast")
        fun retrieveReceiverInfoByIMEI(context: Context): String{
            val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as
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
                TypeEvent.REBOOT ->{ "phone was reboot"}
                TypeEvent.LOCATION -> "Location is"
                TypeEvent.AIRPLANE_MODE -> "user power AIRPLANE_MODE"
                TypeEvent.BATTERY_CHANGE -> "balance battery"
                TypeEvent.CHANGE_STATE_1C -> "1c was closed"
                TypeEvent.LOCATION_BUTTON_OFF -> "user turned off location. GPS don't work"
                TypeEvent.LOCATION_BUTTON_ON -> "user turned on location. GPS work"
                TypeEvent.POWER_ON -> "phone was turned on!!!!"
            }
            return ""
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun retrieveDateFORMATTER(): String{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            return LocalDateTime.now().format(formatter)
        }
    }
}



