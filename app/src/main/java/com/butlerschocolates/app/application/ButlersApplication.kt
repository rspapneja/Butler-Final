package com.butlerschocolates.app.application

import android.app.Application
import android.content.Context

import android.net.ConnectivityManager
import com.butlerschocolates.app.BuildConfig
import com.facebook.stetho.Stetho
import com.google.firebase.crashlytics.FirebaseCrashlytics


class ButlersApplication : Application() {
    val isConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    companion object {
        val TAG = ButlersApplication::class.java.simpleName
        @get:Synchronized
        var instance: ButlersApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance=this

        var crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCrashlyticsCollectionEnabled(true)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}