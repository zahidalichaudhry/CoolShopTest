package com.zahid.coolshoptest.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.zahid.coolshoptest.local_db.dao.UserModelDao
import com.zahid.coolshoptest.local_db.database.AppDatabase
import com.zahid.coolshoptest.local_db.entity.UserModelRoom
import com.zahid.coolshoptest.model.AccountModel
import com.zahid.coolshoptest.model.SessionModel
import com.zahid.coolshoptest.utils.IHandleRoomCallBack
import com.zahid.coolshoptest.utils.NetworkUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception
import java.util.*

object RepositoryLocal {
    private var job: CompletableJob? = null
    private lateinit var network: NetworkUtils

    lateinit var sessionModel: SessionModel
    lateinit var accountModel: AccountModel


    lateinit var mDao: UserModelDao

    fun init(application: Application) {
        network = NetworkUtils(application)
        mDao = AppDatabase.getInstance(application).userModelDao()
    }

    fun getSession(email: String, pass: String, handler: IHandleRoomCallBack<SessionModel>)  {

        if (!network.isConnectedToInternet) {
            handler.onConnectionError()
            return
        }

        job = Job()

        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {

                try {


                    val token: String = (UUID.randomUUID().toString().toUpperCase()+ "|" + "$pass" + "|"
                            + Calendar.getInstance().timeInMillis)
                    var userModelRoom: List<UserModelRoom>? = mDao.checkUserExist(email,pass)
                    if (userModelRoom==null || userModelRoom.isEmpty()){

                       var userModelRoom2 = UserModelRoom(null,email,pass,null,token)
                        var userId:Long = mDao.insert(userModelRoom2)[0]

                        sessionModel = SessionModel(token,userId.toString())

                    }else{
                        mDao.updateToken(userModelRoom.get(0).id!!,token)
                        sessionModel = SessionModel(userModelRoom?.get(0).token!!,userModelRoom.get(0).id.toString())
                    }
                    handler.handleCallBackSuccess(sessionModel)
                }catch (e:Exception){
                    handler.handleCallBackFailure(e.message)
                }


                withContext(Main) {

                    theJob.complete()
                }
            }

        }


    }


    fun postAvatar(avatar: String,token: String, userId: String, handler: IHandleRoomCallBack<AccountModel>)  {

        if (!network.isConnectedToInternet) {
            handler.onConnectionError()
            return
        }

        job = Job()

        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {

                try {
                    var userModelRoom: List<UserModelRoom>? = mDao.checkToken(token)
                    if (userModelRoom==null||userModelRoom.size == 0){
                        handler.handleCallBackFailure("Authorization Failed")
                    }else{
                        mDao.updateAvatar(userId.toLong(),avatar)
                        userModelRoom = mDao.getUser(userId.toLong())
                        accountModel = AccountModel(userModelRoom.get(0).avatar,userModelRoom.get(0).email,userModelRoom.get(0).password)

                        handler.handleCallBackSuccess(accountModel)
                    }

                }catch (e:Exception){
                    handler.handleCallBackFailure(e.message)
                }


                withContext(Main) {

                    theJob.complete()
                }
            }

        }


    }


    fun getUser(token: String,userId: String, handler: IHandleRoomCallBack<AccountModel>) {
        if (!network.isConnectedToInternet) {
            handler.onConnectionError()
            return
        }

        job = Job()

        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {

                try {
                    var userModelRoom: List<UserModelRoom>? = mDao.checkToken(token)
                    if (userModelRoom==null || userModelRoom.size == 0){
                        handler.handleCallBackFailure("Authorization Failed")
                    }else{
                        userModelRoom = mDao.getUser(userId.toLong())
                        accountModel = AccountModel(userModelRoom.get(0).avatar,userModelRoom.get(0).email,userModelRoom.get(0).password)
                        handler.handleCallBackSuccess(accountModel)
                    }

                }catch (e:Exception){
                    handler.handleCallBackFailure(e.message)
                }


                withContext(Main) {

                    theJob.complete()
                }
            }

        }
    }

    fun cancelJobs() {
        job?.cancel()
    }

}
















