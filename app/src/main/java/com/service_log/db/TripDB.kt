package com.service_log.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.service_log.dao.TripDao
import com.service_log.model.Trip

@Database(entities = [Trip::class], version = 1, exportSchema = false)
abstract class TripDB : RoomDatabase() {

        abstract fun abstractTrip() : TripDao

        companion object{
                private var INSTANCE: TripDB? = null
                fun getInstance(context: Context): TripDB{
                        if (INSTANCE == null){
                                INSTANCE = Room.databaseBuilder(
                                        context,
                                        TripDB::class.java,
                                        "TripService.db")
                                        .build()
                        }

                        return INSTANCE as TripDB
                }
        }
}