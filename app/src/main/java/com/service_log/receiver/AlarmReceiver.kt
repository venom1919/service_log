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
import com.service_log.repository.TripRepository
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AlarmReceiver: BroadcastReceiver() {

    private lateinit var dao: TripRepository
    lateinit var retrofitClient: ApiService

    @SuppressLint("SimpleDateFormat")
    override fun onReceive(p0: Context?, p1: Intent?) {

        dao = TripRepository(p0!!)
        retrofitClient = ApiService()

        val trip = Trip(imei = "xx2343", type = TypeEvent.LOCATION , details = "xcx", date ="3423")
        val tripDetails = getDataForServer1c()

        tripDetails.forEach { e -> Log.i("xxxx23", e.details)}

        retrofitClient.retrofitPost().test2(Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
            GlobalAccess.ACCESS_TOKEN, GlobalAccess.AUTH_TOKEN, GlobalAccess.ACCEPT, GlobalAccess.CONTENT_TYPE,
            trip
        ).enqueue(object :
            Callback<PostsResponse>{

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
               Log.i("dont send json", call.isExecuted.toString())
                Log.i("xxxxxxc", call.isExecuted.toString())
                Log.i("ccwr", t.message.toString())
                Log.i("tyt", t.printStackTrace().toString())
                Log.i("3w43", t.cause?.message.toString())
                Log.i("poiyt", t.toString())

            }

            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
//              var code = response.code().toString()

                Log.i("sssq233", response.body().toString())
                Log.i("codeeee" , response.code().toString())
                Log.i("codeefdxc" , response.raw().toString())

            }

            })
    }


    fun getDataForServer1c(): List<Trip> {
         return dao.getAllTrip()
    }

    fun deleteDataIfStatusOk(dao: TripRepository, list: List<Int>){
        dao.deleteTrips(list)
    }

}