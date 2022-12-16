package com.service_log.api

import com.service_log.constant.GlobalAccess
import com.service_log.model.PostsResponse
import com.service_log.model.Trip
import com.squareup.moshi.Json
import retrofit2.http.*

interface RetrofitApi {

    @GET()
    fun sendData(@Header("auth-token") authToken:String,
                 @Header("access-token") accessToken:String,
                 @Header("IMEI") imei:String,
                 @Header("Content-Type") content_Type:String,
//                 @Header("login") login:String,
//                 @Header("passw") passw:String,
                 @Header("Authorization") Authorization:String = "Basic dHNkLnRhYnVyZXRrYS51YToxMnF3YXN6eDIzd2VzZHhj"
                 ): retrofit2.Call<PostsResponse>

    @POST("WriteInfoByTrip")
    fun test(@Header("Authorization") Authorization: String,
             @Header("Access-Token") accessToken:String,
             @Header("auth-token") authToken:String,
             @Header("Accept") Accept:String,
             @Header("Content-Type") Content_Type :String,
             @Body details:Trip
             ): retrofit2.Call<PostsResponse>

    @Headers()
    @POST("WriteInfoByTrip")
    fun test2(): retrofit2.Call<PostsResponse>

}