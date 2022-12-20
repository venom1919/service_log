package com.service_log.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.service_log.dao.TripDao
import com.service_log.model.Trip

@Database(entities = [Trip::class], version = 2, exportSchema = false)
abstract class TripDB : RoomDatabase() {

        abstract fun tripDAO() : TripDao

//        companion object{
//
//                @Volatile private var INSTANCE: TripDB? = null
//
//                private val LOCK = Any()
//
//                operator fun invoke(context: Context)= INSTANCE ?: synchronized(LOCK){
//                        INSTANCE ?: buildDatabase(context).also { INSTANCE = it}
//
////                fun getInstance(context: Context): TripDB{
////                        if (INSTANCE == null){
////                                INSTANCE = Room.databaseBuilder(
////                                        context,
////                                        TripDB::class.java,
////                                        GlobalAccess.DB)
////                                        .build()
////                        }
////
////                        return INSTANCE as TripDB
////                }
//
//
//
//        }
//
//                private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
//                        TripDB::class.java, "TripService.db")
//                        .build()
//        }
}