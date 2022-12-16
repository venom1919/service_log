package com.service_log.db.room
import android.app.Application
import androidx.room.Room
import com.service_log.constant.GlobalAccess
import com.service_log.db.TripDB

class RoomData :Application() {

    val room: TripDB = Room
        .databaseBuilder(this, TripDB::class.java, GlobalAccess.DB)
        .build()
}