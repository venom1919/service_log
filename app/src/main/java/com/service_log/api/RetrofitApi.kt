package com.service_log.api

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApi {

    @POST("/")
    fun <K, V> sendData(@Body requestBody: RequestBody)

}