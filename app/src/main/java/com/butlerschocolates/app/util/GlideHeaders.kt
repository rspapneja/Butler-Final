package com.butlerschocolates.app.util

import android.util.Base64
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders


object GlideHeaders {
    val credentials = AppConstants.USERNAME + ":" + AppConstants.PASSWORD
    val AUTHORIZATION = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)!!
    internal fun getUrlWithHeaders(url: String): GlideUrl {
        return GlideUrl(url, LazyHeaders.Builder()
            .addHeader("Authorization", AUTHORIZATION)
            .build())
    }
}