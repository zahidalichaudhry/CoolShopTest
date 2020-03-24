package com.zahid.coolshoptest.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils(private val context: Context?) {
    val isConnectedToInternet: Boolean
        get() {
            var connected = false
            if (context != null) {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                connected =
                    networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
            }
            return connected
        }

}