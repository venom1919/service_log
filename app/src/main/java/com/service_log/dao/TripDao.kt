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
    suspend fun getAll(): List<Trip>

    @Insert
    suspend fun insertAll(Trips: List<Trip>)

    @Delete
    suspend fun delete(Trip: Trip)

    @Query("SELECT * FROM Trip Where date > :last_time")
    fun getAllWritesAfter(last_time: Date)

    @Query("SELECT * FROM Trip Where date < :last_time")
    fun getLatePosition(last_time: Date)

}