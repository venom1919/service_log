package com.service_log.api

import android.util.Log
import com.service_log.constant.GlobalAccess
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {

    private lateinit var retrofitApi: RetrofitApi

    fun retrofitPost() :RetrofitApi{

        if (!::retrofitApi.isInitialized) {
            Log.i("retrofitdont initi", GlobalAccess.URL)
            val retrofitRes = Retrofit.Builder()
                .baseUrl(GlobalAccess.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofitApi = retrofitRes.create(RetrofitApi::class.java)

            }

        return retrofitApi

    }

}