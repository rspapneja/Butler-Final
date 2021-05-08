package com.butlerschocolates.app.respostiory.storelist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.model.storelist.StoreListApi
import com.butlerschocolates.app.model.storelist.StoreListRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreListRepository {
    val loader: MutableLiveData<Boolean> = MutableLiveData()

    fun getStoreList(context:Context,storeListRequestBody:StoreListRequestBody): MutableLiveData<StoreListApiResponse?> {
        val homeData =  MutableLiveData<StoreListApiResponse?>()
        loader.value = true
        var   apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.getStoreList(storeListRequestBody).enqueue(object : Callback<StoreListApi?> {
            override fun onResponse(
                call: Call<StoreListApi?>,
                response: Response<StoreListApi?>
            ) {
                if (response.isSuccessful) {
                    loader.value=false
                     homeData.postValue(
                         StoreListApiResponse(
                             response.body()
                         )
                     );
                }
            }
            override fun onFailure(call: Call<StoreListApi?>, t: Throwable) {
                 loader.value=false
                 homeData.postValue(
                     StoreListApiResponse(
                         (t)
                     )
                 );
            }
        })
        return homeData
    }

    companion object {
        private var newsRepository: StoreListRepository? = null
        val instance: StoreListRepository?
            get() {
                if (newsRepository == null) {
                    newsRepository =
                        StoreListRepository()
                }
                return newsRepository
            }
    }
}