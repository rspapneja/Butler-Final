package com.butlerschocolates.app.respostiory.verifyotp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.verifyotp.VerifyOtpApi
import com.butlerschocolates.app.model.verifyotp.VerifyOtpRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOtpRepository {
    val loader: MutableLiveData<Boolean> = MutableLiveData()
    fun verifyOtp(context: Context,requestBody: VerifyOtpRequestBody): MutableLiveData<VerfiyOtpApiResponse?> {
        val data = MutableLiveData<VerfiyOtpApiResponse?>()
       //--- loader.value = true
        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.getVerifyOtpResponse(requestBody).enqueue(object : Callback<VerifyOtpApi?> {
            override fun onResponse(
                call: Call<VerifyOtpApi?>,
                response: Response<VerifyOtpApi?>
            ) {
                if (response.code() == 200) {
                   // loader.value = false
                    if (response.body()!!.code == 1||response.body()!!.code == 5||response.body()!!.code == 7) {
                        data.postValue(VerfiyOtpApiResponse(response.body()));
                    } else {
                        data.postValue(VerfiyOtpApiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(VerfiyOtpApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<VerifyOtpApi?>, t: Throwable) {
             //   loader.value = false
                data.postValue(VerfiyOtpApiResponse(t));
            }
        })
        return data
    }

    companion object {
        private var homeRepository: VerifyOtpRepository? = null
        val instance: VerifyOtpRepository?
            get() {
                if (homeRepository == null) {
                    homeRepository =
                        VerifyOtpRepository()
                }
                return homeRepository
            }
    }
}