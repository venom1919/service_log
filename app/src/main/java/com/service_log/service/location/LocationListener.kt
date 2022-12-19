package com.service_log.service.location

import com.google.android.gms.location.LocationResult

interface LocationListener {
    fun locationResponse(locationResult: LocationResult)
}