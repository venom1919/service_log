package com.service_log.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.service_log.dao.AccessDAO
import com.service_log.dao.TripDao
import com.service_log.model.AccessData
import com.service_log.model.Trip


@Database(entities = [Trip::class, AccessData::class], version = 7, exportSchema = false)
abstract class BuilderDB : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun accessDao(): AccessDAO

    companion object {
        private var INSTANCE: BuilderDB? = null

//        val MIGRATION_3_4 = object : Migration(4, 5) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                Log.i("create0000", "1")
//                database.execSQL("CREATE TABLE `AccessData` (`id` INTEGER,`imei` String, `auth_token` String, `access_token` String, `passw` String, `login` String, " +
//                        "PRIMARY KEY(`id`))")
//                Log.i("Finishik", "2")
//
//            }
//        }

        fun getInstance(context: Context): BuilderDB? {
            if (INSTANCE == null) {
                synchronized(BuilderDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BuilderDB::class.java, "TripService.db"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

//        fun migrationDB(context: Context) :BuilderDB?{
//            val MIGRATION_2_3: Migration = object : Migration(3, 4) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    Log.i("dddddddd","22")
//                    database.execSQL("ALTER TABLE Trip ADD COLUMN Test String")
//                }
//            }
//            if (INSTANCE == null) {
//                synchronized(BuilderDB::class) {
//                    Log.i("instance", "")
//                    INSTANCE = Room.databaseBuilder(context.applicationContext,
//                        BuilderDB::class.java, "TripService.db")
//                        .addMigrations(MIGRATION_2_3)
//                        .build()
//                }
//
//            }else
//            return INSTANCE
//            }
//        }
//
//        ///migration column
//        fun migrationDB(context: Context) :BuilderDB?{
//
//            val MIGRATION_2_3: Migration = object : Migration(2, 3) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    Log.i("iiiiiiii", "")
//                    database.execSQL("ALTER TABLE Trip ADD COLUMN User String")
//                }
//            }
//            if (INSTANCE == null) {
//                synchronized(BuilderDB::class) {
//                    Log.i("ssssynchon", "")
//                    INSTANCE = Room.databaseBuilder(context.applicationContext,
//                        BuilderDB::class.java, "TripService.db")
//                        .addMigrations(MIGRATION_2_3)
//                        .build()
//                }
//            }
//            return INSTANCE
//        }

        ///migration table
//        fun migrationDBTable(context: Context) :BuilderDB?{
//
//            Log.i("ssssss122333", "1")
//
//            val MIGRATION_3_4 = object : Migration(4, 5) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    Log.i("create0000", "1")
//                    database.execSQL("CREATE TABLE `AccessData` (`imei` INTEGER, `access_token` String, `passw` String, `login` String, " +
//                            "PRIMARY KEY(`imei`))")
//                    Log.i("Finishik", "2")
//
//                }
//
//
//            }
////            val MIGRATION_2_3: Migration = object : Migration(2, 3) {
////                override fun migrate(database: SupportSQLiteDatabase) {
////                    database.execSQL("ALTER TABLE Access_Data ADD COLUMN Info String")
////                }
////            }
//            if (INSTANCE == null) {
//                synchronized(BuilderDB::class) {
//                    Log.i("migrationsss","12")
//                    INSTANCE = Room.databaseBuilder(context.applicationContext,
//                        BuilderDB::class.java, "TripService.db")
//                        .addMigrations(MIGRATION_3_4)
//                        .build()
//                }
//            }
//            return INSTANCE
//        }


    }

}