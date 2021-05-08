package com.butlerschocolates.app.respostiory.payment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.application.ButlersApplication
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.payment.PaymentApi
import com.butlerschocolates.app.model.payment.PaymentRequestBody
import com.butlerschocolates.app.respostiory.faq.FaqRepository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentRepository {

    fun doCardPayment(requestBody: PaymentRequestBody): MutableLiveData<PaymentApiResponse?> {
        val data = MutableLiveData<PaymentApiResponse?>()

        var apiInterface = ApiClient.getClient(ButlersApplication.instance!!).create(ApiInterface::class.java)

        apiInterface.doCardPayment(requestBody).enqueue(object : Callback<PaymentApi?> {
            override fun onResponse(
                call: Call<PaymentApi?>,
                response: Response<PaymentApi?>
            ) {
                if (response.code() == 200) {
                    if ((response.body()!!.code == 1)||response.body()!!.code == 6){
                        data.postValue(PaymentApiResponse(response.body()));
                    } else {
                        data.postValue(PaymentApiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(PaymentApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<PaymentApi?>, t: Throwable) {
                data.postValue(PaymentApiResponse(t));
            }
        })
        return data
    }

    companion object {
        private var repository: PaymentRepository? = null
        val instance: PaymentRepository?
            get() {
                if (repository == null) {
                    repository =
                        PaymentRepository()
                }
                return repository
            }
    }
}