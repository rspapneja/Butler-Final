package com.butlerschocolates.app.respostiory.faq

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.faq.FaqApi
import com.butlerschocolates.app.model.faq.FaqRequestBady

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqRepository {

    fun getFaq(context: Context, requestBody: FaqRequestBady): MutableLiveData<FaqApiResponse?> {
        val data = MutableLiveData<FaqApiResponse?>()

        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.getFaqs(requestBody).enqueue(object : Callback<FaqApi?> {
            override fun onResponse(
                call: Call<FaqApi?>,
                response: Response<FaqApi?>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.code == 1||response.body()!!.code == 4||response.body()!!.code == 301) {
                        data.postValue(FaqApiResponse(response.body()));
                    } else {
                        data.postValue(FaqApiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(FaqApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<FaqApi?>, t: Throwable) {
               data.postValue(FaqApiResponse(t));
            }
        })
        return data
    }

    companion object {
        private var repository: FaqRepository? = null
        val instance: FaqRepository?
            get() {
                if (repository == null) {
                    repository =
                        FaqRepository()
                }
                return repository
            }
    }
}