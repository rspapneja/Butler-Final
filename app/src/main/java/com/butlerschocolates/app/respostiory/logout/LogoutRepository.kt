package com.butlerschocolates.app.respostiory.logout

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.logout.LogoutApi
import com.butlerschocolates.app.model.logout.LogoutRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutRepository {

    fun logout(context: Context, requestBody: LogoutRequestBody): MutableLiveData<LogoutResponse?> {
        val data = MutableLiveData<LogoutResponse?>()

        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.doLogout(requestBody).enqueue(object : Callback<LogoutApi?> {
            override fun onResponse(
                call: Call<LogoutApi?>,
                response: Response<LogoutApi?>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.code == 1) {
                        data.postValue(LogoutResponse(response.body()));
                    } else {
                        data.postValue(LogoutResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(LogoutResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<LogoutApi?>, t: Throwable) {
               data.postValue(LogoutResponse(t));
            }
        })
        return data
    }

    companion object {
        private var repository: LogoutRepository? = null
        val instance: LogoutRepository?
            get() {
                if (repository == null) {
                    repository =LogoutRepository()
                }
                return repository
            }
    }
}