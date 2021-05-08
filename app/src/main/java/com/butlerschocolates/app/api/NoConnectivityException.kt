package com.butlerschocolates.app.api

import android.content.Context
import com.butlerschocolates.app.R
import java.io.IOException

class NoConnectivityException(context: Context) : IOException() {
    // You can send any message whatever you want from here.
    var context=context
    override val message: String
        get() =context.getString(R.string.no_internetconnection)}