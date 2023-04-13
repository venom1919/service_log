package com.service_log.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "access")
data class AccessData(
                @PrimaryKey(autoGenerate = true) val id: Int? = null,
                @SerializedName("imei") @ColumnInfo(name = "imei") val imei: String,
                @SerializedName("auth_token") @ColumnInfo(name = "auth_token") var auth_token: String,
                @SerializedName("access_token") @ColumnInfo(name = "access_token") var access_token: String,
                @SerializedName("passw") @ColumnInfo(name = "passw") var passw: String,
                @SerializedName("login") @ColumnInfo(name = "login") var login: String,

    )
