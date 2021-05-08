package com.butlerschocolates.app.respostiory.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.signup.SignupApi
import com.butlerschocolates.app.model.signup.SignupRequestBody
import com.butlerschocolates.app.respostiory.signup.SignUpApiResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupRepository {
    val loader: MutableLiveData<Boolean> = MutableLiveData()
    fun getApiResponse(context: Context, requestBody: SignupRequestBody): MutableLiveData<SignUpApiResponse?> {
        val data = MutableLiveData<SignUpApiResponse?>()
        loader.value = true
        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.signupApiResponse(requestBody).enqueue(object : Callback<SignupApi?> {
            override fun onResponse(
                call: Call<SignupApi?>,
                response: Response<SignupApi?>
            ) {
                if (response.code() == 200) {
                    loader.value = false
                    if (response.body()!!.code == 2) {
                        data.postValue(SignUpApiResponse(response.body()))
                    } else  if (response.body()!!.code == 0) {
                        data.postValue(SignUpApiResponse(UnknownError(response.body()!!.data.error)))
                    } else  {
                        data.postValue(SignUpApiResponse(UnknownError(response.body()!!.data.error)))
                    }
                } else {
                    data.postValue(SignUpApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)))
                }
            }

            override fun onFailure(call: Call<SignupApi?>, t: Throwable) {
                loader.value = false
                data.postValue(SignUpApiResponse(t))
            }
        })
        return data
    }

    companion object {
        private var homeRepository: SignupRepository? = null
        val instance: SignupRepository?
            get() {
                if (homeRepository == null) {
                    homeRepository =
                        SignupRepository()
                }
                return homeRepository
            }
    }
}