package com.service_log.api

import retrofit2.Call
import retrofit2.http.POST

interface RetrofitApi {

    @POST("/")
    fun <K, V> sendData(data: Map<K, V>)

    @POST("8RFY")
    fun getCourse(){
//        : Call<?>?
    }

}