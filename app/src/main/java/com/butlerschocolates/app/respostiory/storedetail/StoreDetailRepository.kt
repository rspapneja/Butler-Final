package com.butlerschocolates.app.respostiory.storedetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.storedetail.StoreDetailApi
import com.butlerschocolates.app.model.storedetail.StoreDetailRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreDetailRepository {
    val loader: MutableLiveData<Boolean> = MutableLiveData()

    fun getStoreDetail(context:Context,storeListRequestBody:StoreDetailRequestBody): MutableLiveData<StoreDetailApiResponse?> {
        val data =  MutableLiveData<StoreDetailApiResponse?>()
        loader.value = true
        var   apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.getStoreDetail(storeListRequestBody).enqueue(object : Callback<StoreDetailApi?> {
            override fun onResponse(
                call: Call<StoreDetailApi?>,
                response: Response<StoreDetailApi?>
            ) {
                loader.value=false
                if (response.code() == 200) {
                    if ((response.body()!!.code == 1)||(response.body()!!.code==3)||(response.body()!!.code==301)) {
                        data.postValue(StoreDetailApiResponse(response.body()));
                    }
                    else {
                        data.postValue(StoreDetailApiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(StoreDetailApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<StoreDetailApi?>, t: Throwable) {
                 loader.value=false
                data.postValue(StoreDetailApiResponse( (t) ) );
            }
        })
        return data
    }

    companion object {
        private var newsRepository: StoreDetailRepository? = null
        val instance: StoreDetailRepository?
            get() {
                if (newsRepository == null) {
                    newsRepository =
                        StoreDetailRepository()
                }
                return newsRepository
            }
    }
}