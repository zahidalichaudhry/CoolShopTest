package com.zahid.coolshoptest.utils

import retrofit2.Response

interface IHandleRoomCallBack<T> {
    fun handleCallBackSuccess(response: T)
    fun handleCallBackFailure(error: String?)
    fun onConnectionError()
}