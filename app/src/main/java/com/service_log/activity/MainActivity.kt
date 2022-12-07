package com.service_log.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.service_log.R

open class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

}