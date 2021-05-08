package com.butlerschocolates.app.respostiory.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.home.HomeApi
import com.butlerschocolates.app.model.home.HomeRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    val loader: MutableLiveData<Boolean> = MutableLiveData()

    fun getHomeResponse(context: Context, homeRequestBody: HomeRequestBody): MutableLiveData<HomeApiResponse?> {
        val homeData = MutableLiveData<HomeApiResponse?>()
       // loader.value = true
        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.getHomeApiResponse(homeRequestBody).enqueue(object : Callback<HomeApi?> {
            override fun onResponse(
                call: Call<HomeApi?>,
                response: Response<HomeApi?>
            ) {
                if (response.code() == 200) {
                 //   loader.value = false
                    if (response.body()!!.code == 1) {
                        homeData.postValue(HomeApiResponse(response.body()));
                    } else {
                        homeData.postValue(HomeApiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    homeData.postValue(HomeApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<HomeApi?>, t: Throwable) {
              //  loader.value = false
                homeData.postValue(HomeApiResponse(t));
            }
        })
        return homeData
    }

    companion object {
        private var homeRepository: HomeRepository? = null
        val instance: HomeRepository?
            get() {
                if (homeRepository == null) {
                    homeRepository =
                        HomeRepository()
                }
                return homeRepository
            }
    }
}