package com.service_log.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.service_log.model.AccessData
import com.service_log.model.Trip

@Dao
interface AccessDAO {

    @Insert
    fun insertAccessData(accessData: AccessData)

    @Query("SELECT * FROM access")
    fun getAccessData(): AccessData

    @Delete
    fun deleteAccessData(accessData: AccessData)

    @Query("DELETE From access")
    fun deleteAll()

}