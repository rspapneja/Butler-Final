package com.butlerschocolates.app.respostiory.feedback

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.feedback.FeedbackApi
import com.butlerschocolates.app.model.feedback.FeedbackRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackRepository {

    fun sendFeedback(context: Context,requestBody: FeedbackRequestBody): MutableLiveData<FeedbackApiResponse?> {
        val data = MutableLiveData<FeedbackApiResponse?>()

        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.sendFeedback(requestBody).enqueue(object : Callback<FeedbackApi?> {
            override fun onResponse(
                call: Call<FeedbackApi?>,
                response: Response<FeedbackApi?>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.code == 1||response.body()!!.code == 4||response.body()!!.code == 301) {
                        data.postValue(FeedbackApiResponse(response.body()));
                    } else {
                        data.postValue(FeedbackApiResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(FeedbackApiResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<FeedbackApi?>, t: Throwable) {
               data.postValue(FeedbackApiResponse(t));
            }
        })
        return data
    }

    companion object {
        private var repository: FeedbackRepository? = null
        val instance: FeedbackRepository?
            get() {
                if (repository == null) {
                    repository =
                        FeedbackRepository()
                }
                return repository
            }
    }
}