package com.service_log.repository

import android.content.Context
import android.os.AsyncTask
import com.service_log.dao.TripDao
import com.service_log.db.BuilderDB
import com.service_log.model.Trip

class TripRepository(context: Context) {

    var db : TripDao = BuilderDB.getInstance(context)?.tripDao()!!

    //Fetch All the Trip
    fun getAllTrip(): List<Trip> {
        return db.getAllTrip()
    }

    // Insert new user
    fun insertTrip(users: Trip) {
        insertAsyncTask(db).execute(users)
    }

   /* // update user
    fun updateUser(users: Users) {
        db.updateUser(users)
    }

    // Delete user
    fun deleteUser(users: Users) {
        db.deleteUser(users)
    }*/

    private class insertAsyncTask internal constructor(private val tripDao: TripDao) :
        AsyncTask<Trip, Void, Void>() {

        override fun doInBackground(vararg params: Trip): Void? {
            tripDao.insertAll(params[0])
            return null
        }
    }


}