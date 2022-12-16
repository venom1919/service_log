package com.service_log.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.service_log.api.ApiService
import com.service_log.api.RetrofitApi
import com.service_log.constant.GlobalAccess
import com.service_log.model.PostsResponse
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AlarmReceiver: BroadcastReceiver() {

    lateinit var retrofitClient: ApiService

    override fun onReceive(p0: Context?, p1: Intent?) {

        retrofitClient = ApiService()

        var cr = Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW)

        Log.i("sssss3343", cr)
        Log.i("receiiverrr", "333")
        retrofitClient.retrofitPost().test("Basic dHNkLnRhYnVyZXRrYS51YToxMnF3YXN6eDIzd2VzZHhj",
            GlobalAccess.ACCESS_TOKEN, GlobalAccess.AUTH_TOKEN).enqueue(object :
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