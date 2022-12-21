package com.service_log.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.service_log.model.Trip
import java.util.Date

@Dao
interface TripDao {

    @Query("SELECT * FROM Trip")
    fun getAllTrip(): List<Trip>

    @Insert
    fun insertAll(trip: Trip)

    @Delete
    fun delete(Trip: Trip)

    @Query("SELECT * FROM Trip")
    fun getAllWritesAfter(): List<Trip>

    @Query("SELECT * FROM Trip")
    fun getLatePosition(): List<Trip>



}