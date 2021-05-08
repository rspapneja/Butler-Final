package com.butlerschocolates.app.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.butlerschocolates.app.BuildConfig


object Console {

    fun Log(tag: String, value: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "" + value)
        }
    }

    fun Toast(context: Context, text: String) {

        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}