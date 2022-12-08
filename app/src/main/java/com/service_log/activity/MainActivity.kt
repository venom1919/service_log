package com.service_log.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.service_log.R
import com.service_log.service.GeneralService

open class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("ssssss12", "sssv")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val i = Intent(this, GeneralService::class.java)
        startService(i)

    }
}