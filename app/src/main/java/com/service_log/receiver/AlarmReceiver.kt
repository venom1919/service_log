package com.service_log.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.service_log.api.ApiService
import com.service_log.constant.GlobalAccess
import com.service_log.model.AccessData
import com.service_log.model.AccessResponse
import com.service_log.model.PostsResponse
import com.service_log.model.Trip
import com.service_log.repository.AccessRepository
import com.service_log.repository.TripRepository
import com.service_log.service.static.AssignmentHelper
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmReceiver: BroadcastReceiver() {

    private lateinit var dao: TripRepository
    private lateinit var access: AccessRepository

    private lateinit var retrofitClient: ApiService

    @SuppressLint("SimpleDateFormat")
    override fun onReceive(p0: Context?, p1: Intent?) {

        Log.i("000222","")
        dao = TripRepository(p0!!)
        
        retrofitClient = ApiService()
        access = AccessRepository(p0)
        val imei = AssignmentHelper.retrieveReceiverInfoByIMEI(p0)
//        val accessD = arrayListOf(imei)
        val tripDetails = getDataForServer1c()

       val listId = ArrayList<Int>()
        tripDetails.forEach { e -> e.id?.let { listId.add(it) } }
//
//        deleteDataIfStatusOk(listId)

        if (tripDetails.isEmpty()) {
            Log.i("trip_details", tripDetails.isEmpty().toString())
            return
        }

        ////+++++++++not data about terminal
        if (getAccessForServer1c() == null) {

            Log.i("111","")
             executeActionWithAccess(imei, tripDetails)
//            retrofitClient.retrofitPost().accessFromService(Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
//                GlobalAccess.AUTH_TOKEN, GlobalAccess.ACCEPT, GlobalAccess.CONTENT_TYPE, imei
//            ).enqueue(object : Callback<AccessResponse>{
//
//                override fun onFailure(call: Call<AccessResponse>, t: Throwable) {
//                    t.message?.let {Log.i("errror", it)}
//                }
//                override fun onResponse(call: Call<AccessResponse>, response: Response<AccessResponse>) {
//                    if (response.code() == 200){
//
//                        val loggin = response.body()?.login
//                        val passw = response.body()?.passw
//                        val accesT = response.body()?.access_token
//                        val access = AccessData(imei = imei, auth_token = GlobalAccess.AUTH_TOKEN, access_token = accesT!!, passw = passw!!, login = loggin!!)
//                        saveAccessDataDB(access)
//                        sendRequestLogsTSD(access, tripDetails)
//
//                    }else{
//                        Log.i("ccode___1", response.code().toString() + " " + response.message())
//                    }
//                }
//            })

//            retrofitClient.retrofitPost().accessFromService(
//                Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
//                GlobalAccess.AUTH_TOKEN,
//                GlobalAccess.ACCEPT,
//                GlobalAccess.CONTENT_TYPE,
//                imei,
//                accessD as ArrayList<AccessData>
//            ).enqueue(object : Callback<AccessData> {
//
//                override fun onFailure(call: Call<AccessData>, t: Throwable) {
//                    t.message?.let { Log.i("errror", it) }
//                }
//
//                override fun onResponse(call: Call<AccessData>, response: Response<AccessData>) {
//                    if (response.code() == 200) {
//
//                        Log.i("22222222", "")
//                        val loggin = response.body()?.login
//                        val passw = response.body()?.passw
//                        val accesT = response.body()?.access_token
//                        val access = AccessData(imei = imei, auth_token = GlobalAccess.AUTH_TOKEN, access_token = accesT!!, passw = passw!!, login = loggin!!)
//
//                        Log.i("[ppo[i", access.access_token + " " + access.imei)
//
//                        saveAccessDataDB(access)
//
//                    }else{
//
//                        Log.i("ccode", response.code().toString() + " " + response.message())
//                    }
//                }
//            })
//
//            retrofitClient.retrofitPost().accessFromService(Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
//                GlobalAccess.ACCESS_TOKEN, GlobalAccess.AUTH_TOKEN, GlobalAccess.ACCEPT, GlobalAccess.CONTENT_TYPE,
//                  tripDetails as ArrayList<Trip>
//            ).enqueue(object :
//                Callback<PostsResponse>{
//                override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
//                    t.message?.let {Log.i("errror", it)}
//                }
//                override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
//                    if (response.code() == 200){
//                            val listId = ArrayList<Int>()
//                            tripDetails.forEach {e -> e.id?.let { listId.add(it)}}
//                            deleteDataIfStatusOk(listId)
//                        }else{
//                           Log.i("ccode", response.code().toString() + " " + response.message())
//                       }
//                    }
//                })

        //////-------------not data about terminal
        }else {

            Log.i("222","")
            val lastAccss = access.getLastAccess()
            sendRequestLogsTSD(lastAccss, tripDetails)

//            //////Testing
//            retrofitClient.retrofitPost().sendData1cServer(
//                Credentials.basic(accs.login, accs.passw),
//                accs.access_token,
//                GlobalAccess.AUTH_TOKEN,
//                GlobalAccess.ACCEPT,
//                GlobalAccess.CONTENT_TYPE,
//                tripDetails as ArrayList<Trip>
//            ).enqueue(object :
//                Callback<PostsResponse> {
//                override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
//                    t.message?.let { Log.i("errror", it) }
//                }
//
//                override fun onResponse(
//                    call: Call<PostsResponse>,
//                    response: Response<PostsResponse>
//                ) {
//                    if (response.code() == 200) {
//                        val listId = ArrayList<Int>()
//                        tripDetails.forEach { e -> e.id?.let { listId.add(it) } }
//                        deleteDataIfStatusOk(listId)
//                    }
//                }
//            })
//                retrofitClient.retrofitPost().sendData1cServer(Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
//                GlobalAccess.ACCESS_TOKEN, GlobalAccess.AUTH_TOKEN, GlobalAccess.ACCEPT, GlobalAccess.CONTENT_TYPE,
//                  tripDetails as ArrayList<Trip>
//            ).enqueue(object :
//                Callback<PostsResponse>{
//                override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
//                    t.message?.let {Log.i("errror", it)}
//                }
//                override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
//                    if (response.code() == 200){
//                            val listId = ArrayList<Int>()
//                            tripDetails.forEach {e -> e.id?.let { listId.add(it)}}
//                            deleteDataIfStatusOk(listId)
//                        }
//                    }
//                })
        }
    }

    fun sendRequestLogsTSD(accs: AccessData, tripDetails:List<Trip>) {

        Log.i("333","")
            retrofitClient.retrofitPost().sendData1cServer(
                Credentials.basic(accs.login, accs.passw),
                accs.access_token,
                GlobalAccess.AUTH_TOKEN,
                GlobalAccess.ACCEPT,
                GlobalAccess.CONTENT_TYPE,
                accs.imei,
                tripDetails as ArrayList<Trip>
            ).enqueue(object :
                Callback<PostsResponse> {
                override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                    t.message?.let {Log.i("errror", it) }
                }

                override fun onResponse(
                    call: Call<PostsResponse>,
                    response: Response<PostsResponse>) {
                        if (response.code() == 200) {
                            val listId = ArrayList<Int>()
                            tripDetails.forEach { e -> e.id?.let { listId.add(it) } }
                            deleteDataIfStatusOk(listId)
                        }else if(response.code() == 403){
                            deleteAllAccess()
                            executeActionWithAccess(accs.imei, tripDetails)
                        }else{
                            deleteAllAccess()
                            executeActionWithAccess(accs.imei, tripDetails)
                        }
                }
            })
    }

    fun executeActionWithAccess(imei:String, tripDetails: List<Trip>){

        retrofitClient.retrofitPost().accessFromService(Credentials.basic(GlobalAccess.LOGIN, GlobalAccess.PASSW),
            GlobalAccess.AUTH_TOKEN, GlobalAccess.ACCEPT, GlobalAccess.CONTENT_TYPE, imei
        ).enqueue(object : Callback<AccessResponse>{

            override fun onFailure(call: Call<AccessResponse>, t: Throwable) {
                t.message?.let {Log.i("errror", it)}
            }

            override fun onResponse(call: Call<AccessResponse>, response: Response<AccessResponse>) {
                if (response.code() == 200){

                    val loggin = response.body()?.login
                    val passw = response.body()?.passw
                    val accesT = response.body()?.access_token
                    val access = AccessData(imei = imei, auth_token = GlobalAccess.AUTH_TOKEN, access_token = accesT!!, passw = passw!!, login = loggin!!)
                    saveAccessDataDB(access)
                    sendRequestLogsTSD(access, tripDetails)

                }else{
                    Log.i("response_code", response.code().toString())
                }
            }
        })
    }

    fun getDataForServer1c(): List<Trip> {
        return dao.getAllTrip()
    }

    fun getAccessForServer1c() : AccessData{
        return access.getLastAccess()
    }

    fun saveAccessDataDB(accessData: AccessData) {
        access.saveAccessDB(accessData)
    }

    fun deleteDataIfStatusOk(list: List<Int>) {
        dao.deleteTrips(list)
    }

    fun deleteAccess(accessData: AccessData){
        access.deleteAccess(accessData)
    }

    fun deleteAllAccess(){
        access.deleteALL()
    }

}
