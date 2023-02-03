package com.service_log.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.service_log.dao.TripDao
import com.service_log.model.Trip


@Database(entities = [Trip::class], version = 3, exportSchema = false)
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

//        fun migrationDB(context: Context) :BuilderDB?{
//
//            val MIGRATION_2_3: Migration = object : Migration(2, 3) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    database.execSQL("ALTER TABLE Trip ADD COLUMN Info String")
//                }
//            }
//            if (INSTANCE == null) {
//                synchronized(BuilderDB::class) {
//                    INSTANCE = Room.databaseBuilder(context.applicationContext,
//                        BuilderDB::class.java, "TripService.db")
//                        .addMigrations(MIGRATION_2_3)
//                        .build()
//                }
//            }
//            return INSTANCE
//        }
    }

}