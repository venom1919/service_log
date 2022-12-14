package com.service_log.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.service_log.enums.TypeEvent
import java.util.Date

@Entity(tableName = "trip")
data class Trip(@PrimaryKey(autoGenerate = true) val id: Int,
           @ColumnInfo(name = "imei") val imei:String,
           @ColumnInfo(name = "type") val type:TypeEvent,
           @ColumnInfo(name = "details") val details:String,
           @ColumnInfo(name = "date") val date:Date){

}
