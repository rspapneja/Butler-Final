package com.butlerschocolates.app.respostiory.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.login.LoginApi
import com.butlerschocolates.app.model.login.LoginReqestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    fun login(context: Context, requestBody: LoginReqestBody): MutableLiveData<LoginApiiResponse?> {
        val data = MutableLiveData<LoginApiiResponse?>()

        var apiInterface = ApiClient.getClient(context ).create(ApiInterface::class.java)
        apiInterface.doLogin(requestBody).enqueue(object : Callback<LoginApi?> {
            override fun onResponse(
                call: Call<LoginApi?>,
                response: Response<LoginApi?>
            ) {
                if (response.code() == 200) {
                    if ((response.body()!!.code == 1)||(response.body()!!.code==3)||(response.body()!!.code==7)||(response.body()!!.code==2)) {
                        data.postValue(LoginApiiResponse(response.body()));
                    }
                    else {
                        data.postValue(LoginApiiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(LoginApiiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<LoginApi?>, t: Throwable) {
               data.postValue(LoginApiiResponse(t));
            }
        })
        return data
    }

    companion object {
        private var repository: LoginRepository? = null
        val instance: LoginRepository?
            get() {
                if (repository == null) {
                    repository =LoginRepository()
                }
                return repository
            }
    }
}