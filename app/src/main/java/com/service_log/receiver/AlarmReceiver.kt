package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.service_log.api.ApiService
import com.service_log.constant.GlobalAccess
import com.service_log.enums.TypeEvent
import com.service_log.model.PostsResponse
import com.service_log.model.Trip
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.log

class AlarmReceiver: BroadcastReceiver() {

    lateinit var retrofitClient: ApiService

    @SuppressLint("SimpleDateFormat")
    override fun onReceive(p0: Context?, p1: Intent?) {

        retrofitClient = ApiService()

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        var tripak = Trip(1, "22", TypeEvent.LOCATION,"details", currentDate)

        Log.i("rrr", tripak.toString())
        retrofitClient.retrofitPost().test(Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
            GlobalAccess.ACCESS_TOKEN, GlobalAccess.AUTH_TOKEN, GlobalAccess.ACCEPT, GlobalAccess.CONTENT_TYPE, tripak).enqueue(object :
            Callback<PostsResponse>{

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
               Log.i("errror", call.toString())
            }

            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
//              var code = response.code().toString()

                Log.i("sssq233", response.body().toString())
                Log.i("codeeee" , response.code().toString())
                Log.i("codeefdxc" , response.raw().toString())

            }

            })
    }

}