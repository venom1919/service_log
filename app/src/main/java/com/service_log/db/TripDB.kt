package com.service_log.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.service_log.dao.TripDao
import com.service_log.model.Trip

@Database(entities = [Trip::class], version = 2, exportSchema = false)
abstract class TripDB : RoomDatabase() {
        abstract fun tripDAO() : TripDao
}