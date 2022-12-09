package com.service_log.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.service_log.model.Trip

@Database(entities = [Trip::class], version = 1, exportSchema = false)
abstract class TripDB : RoomDatabase() {



}