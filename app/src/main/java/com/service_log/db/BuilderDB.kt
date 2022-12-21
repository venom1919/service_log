package com.service_log.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.service_log.dao.TripDao
import com.service_log.model.Trip

@Database(entities = [Trip::class], version = 2, exportSchema = false)
abstract class BuilderDB : RoomDatabase() {

    abstract fun tripDao() : TripDao

    companion object {
        private var INSTANCE: BuilderDB? = null

        fun getInstance(context: Context): BuilderDB? {
            if (INSTANCE == null) {
                synchronized(BuilderDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        BuilderDB::class.java, "TripService.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }


//    private var INSTANCE: GfgDatabase? = null
//    fun getInstance(context: Context): GfgDatabase {
//        if (INSTANCE == null) {
//            synchronized(GfgDatabase::class) {
//                INSTANCE = buildRoomDB(context)
//            }
//        }
//        return INSTANCE!!
//    }
//    private fun buildRoomDB(context: Context) =
//        Room.databaseBuilder(
//            context.applicationContext,
//            GfgDatabase::class.java,
//            "geeksforgeeks-example-coroutines"
//        ).build()
}