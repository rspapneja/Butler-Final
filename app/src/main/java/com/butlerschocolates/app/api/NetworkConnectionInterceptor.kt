package com.butlerschocolates.app.api

import android.content.Context
import com.butlerschocolates.app.application.ButlersApplication
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class NetworkConnectionInterceptor(context: Context) : Interceptor {
    private val mContext: Context

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!ButlersApplication.instance!!.isConnected) {
            throw NoConnectivityException(mContext)
            // Throwing our custom exception 'NoConnectivityException'
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
    init {
        mContext = context
    }
}