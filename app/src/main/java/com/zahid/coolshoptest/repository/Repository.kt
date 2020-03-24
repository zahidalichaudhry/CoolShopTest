package com.zahid.coolshoptest.repository

import android.app.Application
import com.huaweiHMSdemo.huawei.api.MyRetrofitBuilder
import com.zahid.coolshoptest.model.AccountModel
import com.zahid.coolshoptest.model.SessionModel
import com.zahid.coolshoptest.model.general.GeneralResponseModel
import com.zahid.coolshoptest.utils.IHandleAPICallBack
import com.zahid.coolshoptest.utils.NetworkUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Repository {
    private var job: CompletableJob? = null
    private lateinit var network: NetworkUtils

    fun init(application: Application) {
        network = NetworkUtils(application)
    }

    fun getSession(email: String, pass: String, handler: IHandleAPICallBack<GeneralResponseModel<SessionModel>>) {


        if (!network.isConnectedToInternet) {
            handler.onConnectionError()
            return
        }

        job = Job()

        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {
                try {
                    val getSession = MyRetrofitBuilder.apiService.login(email,pass)
                    getSession.enqueue(object :
                        Callback<GeneralResponseModel<SessionModel>> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel<SessionModel>>,
                            response: Response<GeneralResponseModel<SessionModel>>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.code==200&&response.body()?.status==true){
                                    handler.handleWebserviceCallBackSuccess(response)
                                }else{
                                    handler.handleWebserviceCallBackFailureresult(response.body()?.message)
                                }
                            } else {
                                // Handle error returned from server
                                handler.handleWebserviceCallBackFailure(
                                    response.errorBody().toString()
                                )
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponseModel<SessionModel>>, t: Throwable) {
                            t.printStackTrace()
                            handler.handleWebserviceCallBackFailure(t.message)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    handler.handleWebserviceCallBackFailure(e.message)
                }
                withContext(Main) {
                    theJob.complete()
                }
            }

        }


    }

    fun postAvatar(token: String,userId: String, avatar: String, handler: IHandleAPICallBack<GeneralResponseModel<AccountModel>>) {


        if (!network.isConnectedToInternet) {
            handler.onConnectionError()
            return
        }

        job = Job()

        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {
                try {
                    val getSession = MyRetrofitBuilder.apiService.postAvator("Bearer $token",userId,avatar)
                    getSession.enqueue(object :
                        Callback<GeneralResponseModel<AccountModel>> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel<AccountModel>>,
                            response: Response<GeneralResponseModel<AccountModel>>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.code==200&&response.body()?.status==true){
                                    handler.handleWebserviceCallBackSuccess(response)
                                }else{
                                    handler.handleWebserviceCallBackFailureresult(response.body()?.message)
                                }
                            } else {
                                // Handle error returned from server
                                handler.handleWebserviceCallBackFailure(
                                    response.errorBody().toString()
                                )
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponseModel<AccountModel>>, t: Throwable) {
                            t.printStackTrace()
                            handler.handleWebserviceCallBackFailure(t.message)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    handler.handleWebserviceCallBackFailure(e.message)
                }
                withContext(Main) {
                    theJob.complete()
                }
            }

        }


    }

    fun getUser(token: String,userId: String, handler: IHandleAPICallBack<GeneralResponseModel<AccountModel>>) {


        if (!network.isConnectedToInternet) {
            handler.onConnectionError()
            return
        }

        job = Job()

        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {
                try {
                    val getSession = MyRetrofitBuilder.apiService.getUser("Bearer $token",userId)
                    getSession.enqueue(object :
                        Callback<GeneralResponseModel<AccountModel>> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel<AccountModel>>,
                            response: Response<GeneralResponseModel<AccountModel>>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.code==200&&response.body()?.status==true){
                                    handler.handleWebserviceCallBackSuccess(response)
                                }else{
                                    handler.handleWebserviceCallBackFailureresult(response.body()?.message)
                                }
                            } else {
                                // Handle error returned from server
                                handler.handleWebserviceCallBackFailure(
                                    response.errorBody().toString()
                                )
                            }
                        }

                        override fun onFailure(call: Call<GeneralResponseModel<AccountModel>>, t: Throwable) {
                            t.printStackTrace()
                            handler.handleWebserviceCallBackFailure(t.message)
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    handler.handleWebserviceCallBackFailure(e.message)
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
















