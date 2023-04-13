package com.service_log.api

import com.service_log.model.AccessData
import com.service_log.model.AccessResponse
import com.service_log.model.PostsResponse
import com.service_log.model.Trip
import com.squareup.moshi.Json
import retrofit2.http.*

interface RetrofitApi {

//    @GET()
//    fun sendData(@Header("auth-token") authToken:String,
//                 @Header("access-token") accessToken:String,
//                 @Header("IMEI") imei:String,
//                 @Header("Content-Type") content_Type:String,
//                 @Header("Authorization") Authorization:String
//                 ): retrofit2.Call<PostsResponse>

    @POST("PostInfoByTrip")
    fun sendData1cServer(@Header("Authorization") Authorization: String,
                         @Header("Access-Token") accessToken: String,
                         @Header("auth-token") authToken: String,
                         @Header("Accept") Accept: String,
                         @Header("Content-Type") Content_Type: String, @Header("imei") imei: String,
                         @Body details: ArrayList<Trip>
    ): retrofit2.Call<PostsResponse>

//    @POST("AccessTSDByIMEI")
//    fun accessFromService(
//        @Header("Authorization") Authorization: String,
//        @Header("auth-token") authToken: String,
//        @Header("Accept") Accept: String,
//        @Header("Content-Type") Content_Type: String,
//        @Header("imei") imei: String,
//        @Body accessD:ArrayList<AccessData>
////        @Body imei: ArrayList<String>,
//    ): retrofit2.Call<AccessData>
@POST("AccessTSDByIMEI")
    fun accessFromService(
    @Header("Authorization") Authorization: String,
//    @Header("Access-Token") accessToken: String,
    @Header("auth-token") authToken: String,
    @Header("Accept") Accept: String,
    @Header("Content-Type") Content_Type: String,
    @Header("imei") imei: String
): retrofit2.Call<AccessResponse>

    @POST("AccessWrite")
    fun accessWrite(): retrofit2.Call<AccessData>

}