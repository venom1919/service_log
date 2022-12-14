package com.service_log.model

import java.lang.Math.*
import kotlin.math.pow

class Location( val pcode: Int,
                val locality: String,
                val state: String,
                val comments: String,
                val category: String,
                val longitude: Double,
                val latitude: Double,) {
}

fun distance(fromLat: Double, fromLon: Double, toLat: Double, toLon: Double): Double {

    val radius = 6378137.0
    val deltaLat = toLat - fromLat
    val deltaLon = toLon - fromLon
    val angle = 2 * asin(
        sqrt(
            sin(deltaLat / 2).pow(2.0) + cos(fromLat) * cos(toLat) * sin(deltaLon / 2).pow(2.0)
        )
    )
    return radius * angle
}

fun logicDistance(latit : Int = 0, long : Int = 0 ){




}



