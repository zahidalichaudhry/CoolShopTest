package com.zahid.coolshoptest.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.zahid.coolshoptest.repository.Repository
import com.zahid.coolshoptest.managers.AppManager
import com.zahid.coolshoptest.model.AccountModel
import com.zahid.coolshoptest.model.SessionModel
import com.zahid.coolshoptest.model.general.GeneralResponseModel
import com.zahid.coolshoptest.utils.IHandleAPICallBack
import com.zahid.coolshoptest.utils.ResultWrapper
import com.zahid.coolshoptest.utils.Status
import retrofit2.Response


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var _application: Application = application

    private var repository: Repository = Repository

    var appManager: AppManager = AppManager(application)

    var getSessionModel: MutableLiveData<ResultWrapper<SessionModel>> =
        MutableLiveData()

    var getUserData: MutableLiveData<ResultWrapper<AccountModel>> =
        MutableLiveData()

    fun loadFromSession(){
        getUserData.postValue(
            ResultWrapper(
                Status.status.SUCCESS,
                appManager.persistenceManager.loggedInUser, "Loading"
            )
        )
    }

    fun getSession(email: String, pass: String) {

        repository.init(_application)

        getSessionModel.postValue(
            ResultWrapper(
                Status.status.LOADING,
                null, "Loading"
            )
        )
        repository.getSession(email, pass, object
            : IHandleAPICallBack<GeneralResponseModel<SessionModel>> {
            override fun handleWebserviceCallBackSuccess(response: Response<GeneralResponseModel<SessionModel>>?) {

                getSessionModel.postValue(
                    ResultWrapper<SessionModel>(
                        Status.status.SUCCESS,
                        response?.body()?.data,
                        "Success"
                    )
                )

                val accountModel = AccountModel(null,null,pass)
                appManager.persistenceManager.saveLoggedInAccount(accountModel)
                appManager.persistenceManager.saveSession(response?.body()?.data)
            }

            override fun handleWebserviceCallBackFailure(error: String?) {
                getSessionModel.postValue(
                    error?.let {
                        ResultWrapper<SessionModel>(
                            Status.status.ERROR,
                            null,
                            it
                        )
                    }
                )
            }

            override fun handleWebserviceCallBackFailureresult(error: String?) {

                getSessionModel.postValue(
                    error?.let {
                        ResultWrapper<SessionModel>(
                            Status.status.NORESULT,
                            null,
                            it
                        )
                    })
            }

            override fun onConnectionError() {
                getSessionModel.postValue(
                    ResultWrapper<SessionModel>(
                        Status.status.ERROR,
                        null,
                        "Connection Error"
                    )

                )
            }
        })


    }

    fun postAvatar(uri: Bitmap) {

        val encodedString: String = appManager.mediaManager.encodedBitmap(uri)


        repository.init(_application)

        getSessionModel.postValue(
            ResultWrapper(
                Status.status.LOADING,
                null, "Loading"
            )
        )
        repository.postAvatar(appManager.persistenceManager.session.token,
            appManager.persistenceManager.session.userid,
            encodedString, object
                : IHandleAPICallBack<GeneralResponseModel<AccountModel>> {
                override fun handleWebserviceCallBackSuccess(response: Response<GeneralResponseModel<AccountModel>>?) {
                    appManager.persistenceManager.updateImagUrl(response?.body()?.data)
                    getUserData.postValue(
                        ResultWrapper<AccountModel>(
                            Status.status.SUCCESS,
                            appManager.persistenceManager.loggedInUser,
                            "Success"
                        )
                    )
                }

                override fun handleWebserviceCallBackFailure(error: String?) {
                    getUserData.postValue(
                        error?.let {
                            ResultWrapper<AccountModel>(
                                Status.status.ERROR,
                                appManager.persistenceManager.loggedInUser,
                                it
                            )
                        }
                    )
                }

                override fun handleWebserviceCallBackFailureresult(error: String?) {

                    getUserData.postValue(
                        error?.let {
                            ResultWrapper<AccountModel>(
                                Status.status.NORESULT,
                                appManager.persistenceManager.loggedInUser,
                                it
                            )
                        })
                }

                override fun onConnectionError() {
                    getUserData.postValue(
                        ResultWrapper<AccountModel>(
                            Status.status.ERROR,
                            appManager.persistenceManager.loggedInUser,
                            "Connection Error"
                        )

                    )
                }
            })


    }


    fun getUser() {
        repository.init(_application)
        getSessionModel.postValue(
            ResultWrapper(
                Status.status.LOADING,
                null, "Loading"
            )
        )
        repository.getUser(appManager.persistenceManager.session.token,
            appManager.persistenceManager.session.userid, object
                : IHandleAPICallBack<GeneralResponseModel<AccountModel>> {
                override fun handleWebserviceCallBackSuccess(response: Response<GeneralResponseModel<AccountModel>>?) {
                    appManager.persistenceManager.updateEmail(response?.body()?.data)
                    appManager.persistenceManager.updateImagUrl(response?.body()?.data)
                    getUserData.postValue(
                        ResultWrapper(
                            Status.status.SUCCESS,
                            appManager.persistenceManager.loggedInUser,
                            "Success"
                        )
                    )
                }

                override fun handleWebserviceCallBackFailure(error: String?) {
                    getUserData.postValue(
                        error?.let {
                            ResultWrapper(
                                Status.status.ERROR,
                                appManager.persistenceManager.loggedInUser,
                                it
                            )
                        }
                    )
                }

                override fun handleWebserviceCallBackFailureresult(error: String?) {

                    getUserData.postValue(
                        error?.let {
                            ResultWrapper(
                                Status.status.NORESULT,
                                appManager.persistenceManager.loggedInUser,
                                it
                            )
                        })
                }

                override fun onConnectionError() {
                    getUserData.postValue(
                        ResultWrapper(
                            Status.status.ERROR,
                            appManager.persistenceManager.loggedInUser,
                            "Connection Error"
                        )

                    )
                }
            })


    }

    fun cancelJobs() {
        Repository.cancelJobs()
    }
}