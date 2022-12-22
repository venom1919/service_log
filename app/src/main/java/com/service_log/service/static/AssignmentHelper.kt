package com.service_log.service.static

import com.service_log.enums.TypeEvent

class AssignmentHelper {

    companion object {

        fun retrieveIMEI(): String{
            return ""
        }

        fun retrieveDETAILS(typeEvent: TypeEvent): String{
            when(typeEvent){
                TypeEvent.REBOOT -> "phone was reboot"
                TypeEvent.LOCATION -> ""
                TypeEvent.AIRPLANE_MODE -> "user power AIRPLANE_MODE"
                TypeEvent.BATTERY_CHANGE -> "balance battery"
                TypeEvent.CHANGE_STATE_1C -> ""
                TypeEvent.LOCATION_BUTTON_OFF -> ""
                TypeEvent.LOCATION_BUTTON_ON -> ""
                TypeEvent.POWER_ON -> "phone was turned on!!!!"
            }
            return ""
        }

        fun retrieveDATEFORMATTER(): String{
            return ""
        }
    }
}



