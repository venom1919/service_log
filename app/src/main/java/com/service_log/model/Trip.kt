package com.service_log.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.service_log.enums.TypeEvent

@Entity(tableName = "trip")
data class Trip(@PrimaryKey(autoGenerate = true) val id: Int ? = null  ,
                @SerializedName("imei") @ColumnInfo(name = "imei") val imei:String,
                @SerializedName("type") @ColumnInfo(name = "type") val type:TypeEvent,
                @SerializedName("details") @ColumnInfo(name = "details") val details:String,
                @SerializedName("date") @ColumnInfo(name = "date") val date: String,
                @SerializedName("info") @ColumnInfo(name = "Info") val info: String

){


}
