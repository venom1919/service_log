package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.service_log.api.ApiService
import com.service_log.constant.GlobalAccess
import com.service_log.model.PostsResponse
import com.service_log.model.Trip
import com.service_log.repository.TripRepository
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmReceiver: BroadcastReceiver() {

    private lateinit var dao: TripRepository
    private lateinit var retrofitClient: ApiService

    @SuppressLint("SimpleDateFormat")
    override fun onReceive(p0: Context?, p1: Intent?) {

        dao = TripRepository(p0!!)
        retrofitClient = ApiService()

        val tripDetails = getDataForServer1c()

        if (tripDetails.isEmpty())
            return
        retrofitClient.retrofitPost().sendData1cServer(Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
            GlobalAccess.ACCESS_TOKEN, GlobalAccess.AUTH_TOKEN, GlobalAccess.ACCEPT, GlobalAccess.CONTENT_TYPE,
              tripDetails as ArrayList<Trip>
        ).enqueue(object :
            Callback<PostsResponse>{
            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                if (response.code() == 200){
                    val listId = ArrayList<Int>()
                    tripDetails.forEach {e -> e.id?.let { listId.add(it)}}
                    deleteDataIfStatusOk(listId)
                }
            }
            })
    }
    fun getDataForServer1c(): List<Trip> {
         return dao.getAllTrip()
    }

    fun deleteDataIfStatusOk(list: List<Int>){
        dao.deleteTrips(list)
    }
}
