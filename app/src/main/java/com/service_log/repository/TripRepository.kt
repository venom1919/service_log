package com.service_log.repository

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.service_log.dao.TripDao
import com.service_log.db.BuilderDB
import com.service_log.model.Trip


class TripRepository(context: Context) {

    val context = context
    var db : TripDao = BuilderDB.getInstance(context)?.tripDao()!!
//
//    fun updateData(){
//        BuilderDB.migrationDB(context)
//    }

    //Fetch All the Trip
    fun getAllTrip(): List<Trip> {
        return db.getAllTrip()
    }

    // Insert new trip
    fun insertTrip(users: Trip) {
        insertAsyncTask(db).execute(users)
    }

    // Delete trips
    fun deleteTrips(list: List<Int>) {
        db.deleteDataId()
    }

    private class insertAsyncTask internal constructor(private val tripDao: TripDao) :
        AsyncTask<Trip, Void, Void>() {

        override fun doInBackground(vararg params: Trip): Void? {
            tripDao.insertAll(params[0])
            return null
        }
    }

}