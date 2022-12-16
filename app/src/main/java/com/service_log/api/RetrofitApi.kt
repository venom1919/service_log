package com.service_log.api

import com.service_log.constant.GlobalAccess
import com.service_log.model.PostsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

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
             @Header("Accept") Accept:String = "application/json, text/plain, */*",
             @Header("Content-Type") Content_Type :String = "application/json;charset=utf-8"
             ): retrofit2.Call<PostsResponse>

    @Headers()
    @POST("WriteInfoByTrip")
    fun test2(): retrofit2.Call<PostsResponse>

}