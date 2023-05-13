package com.service_log.repository

import android.content.Context
import android.util.Log
import com.service_log.dao.AccessDAO
import com.service_log.db.BuilderDB
import com.service_log.model.AccessData
import com.service_log.service.static.AssignmentHelper

class AccessRepository(var context: Context) {

    var db : AccessDAO = BuilderDB.getInstance(context)?.accessDao()!!

    fun getAccess() : Map<String, AccessData>{
        val map = mutableMapOf<String, AccessData>()
        map[AssignmentHelper.retrieveReceiverInfoByIMEI(context)] = db.getAccessData()
        return map
    }

    fun saveAccessDB(accessData: AccessData){
        db.insertAccessData(accessData)
    }

    fun deleteAccess(accessData: AccessData){
        db.deleteAccessData(accessData)
    }

    fun getLastAccess() : AccessData{
        return db.getAccessData()
    }

    fun deleteALL(){
        db.deleteAll()
    }
}