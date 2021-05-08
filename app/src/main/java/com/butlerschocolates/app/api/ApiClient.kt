package com.butlerschocolates.app.api

import android.content.Context
import android.util.Base64
import com.butlerschocolates.app.BuildConfig
import com.butlerschocolates.app.api.NetworkConnectionInterceptor
import com.butlerschocolates.app.application.ButlersApplication
import com.butlerschocolates.app.util.AppConstants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object ApiClient {

    val API_BASE_URL = AppConstants.BASE_URL
    fun getClient(context: Context): Retrofit  {

            val httpLoggingInterceptor = HttpLoggingInterceptor()

            if (BuildConfig.DEBUG) {
            // development build
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
            // production build
           //  httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            }

              val dispatcher = Dispatcher()
              dispatcher.maxRequests = 1

            val httpClient = OkHttpClient.Builder()
             if (BuildConfig.DEBUG) {
            httpClient.interceptors().add(httpLoggingInterceptor)
            httpClient.addNetworkInterceptor(StethoInterceptor())
            }
            httpClient.addInterceptor(NetworkConnectionInterceptor(ButlersApplication.instance!!))


            val gson = GsonBuilder()
                .setLenient()
                .create()

            val builder = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))

            if (AppConstants.USERNAME != null && AppConstants.PASSWORD != null) {
                val credentials = AppConstants.USERNAME + ":" + AppConstants.PASSWORD
                val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

                httpClient.addInterceptor { chain ->
                    val original = chain.request()

                    val requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .header("Accept", "application/json")
                        .method(original.method, original.body)

                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
            }
            httpClient.connectTimeout(1, TimeUnit.MINUTES)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
             httpClient.dispatcher(dispatcher)
            val client = httpClient.build()
            return builder.client(client).build()
        }

    val MULTIPART_FORM_DATA = "multipart/form-data"

    fun createRequestBody(s: String): RequestBody {
        return RequestBody.create(MULTIPART_FORM_DATA.toMediaTypeOrNull(), s)
    }
}