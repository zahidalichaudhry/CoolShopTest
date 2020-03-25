package com.zahid.coolshoptest.managers

import android.app.Application
import android.content.Context
import android.util.Log

class AppManager(application: Application) {
    val context: Context
    var persistenceManager: PersistenceManager
    val mediaManager: MediaManager

    init {
        context = application.applicationContext
        persistenceManager = PersistenceManager(context)
        Log.e("AppManager", "in")
        mediaManager = MediaManager(context)
    }
}