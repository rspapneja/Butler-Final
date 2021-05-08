package com.butlerschocolates.app.respostiory.storetimimg

import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.application.ButlersApplication
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.storetiming.StoreTimingAPI
import com.butlerschocolates.app.model.storetiming.StoreTimingRequestBody
import com.butlerschocolates.app.respostiory.base.BaseRepository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreTimingRepository:BaseRepository() {
    var apiInterface = ApiClient.getClient(ButlersApplication!!.instance!!).create(ApiInterface::class.java)

    fun geStoreTimingResponse(requestBody: StoreTimingRequestBody): MutableLiveData<StoreTimingApiResponse?> {

      val data = MutableLiveData<StoreTimingApiResponse?>()
      apiInterface.getStoreTimimg(requestBody).enqueue(object : Callback<StoreTimingAPI?> {
            override fun onResponse(
                call: Call<StoreTimingAPI?>,
                response: Response<StoreTimingAPI?>
            ) {
                if (response.code() == 200) {
                    if ((response.body()!!.code == 1)||response.body()!!.code == 6||response.body()!!.code == 4||response.body()!!.code == 301){
                        data.postValue(StoreTimingApiResponse(response.body()));
                    } else {
                        data.postValue(StoreTimingApiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(StoreTimingApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<StoreTimingAPI?>, t: Throwable) {
                data.postValue(StoreTimingApiResponse(t));
            }
        })
        return data
    }

    companion object {
        private var repository: StoreTimingRepository? = null
        val instance: StoreTimingRepository?
            get() {
                if (repository == null) {
                    repository =
                        StoreTimingRepository()
                }
                return repository
            }
    }
}