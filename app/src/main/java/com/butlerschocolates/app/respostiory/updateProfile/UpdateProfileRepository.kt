package com.butlerschocolates.app.respostiory.updateProfile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.ApiInterface
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.updateProfile.UpdateProfileApi

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.PartMap

class UpdateProfileRepository {

    fun updateProfile(context: Context,@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>): MutableLiveData<UpdateProfileResponse?> {
        val data = MutableLiveData<UpdateProfileResponse?>()

        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.updateProfile(map).enqueue(object : Callback<UpdateProfileApi?> {
            override fun onResponse(
                call: Call<UpdateProfileApi?>,
                response: Response<UpdateProfileApi?>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.code == 1||response.body()!!.code == 4||response.body()!!.code == 301) {
                        data.postValue(UpdateProfileResponse(response.body()));
                    } else {
                        data.postValue(UpdateProfileResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(UpdateProfileResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<UpdateProfileApi?>, t: Throwable) {
               data.postValue(UpdateProfileResponse(t));
            }
        })
        return data
    }

    fun updateProfile(context: Context,@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: Array<MultipartBody.Part?>): MutableLiveData<UpdateProfileResponse?> {
        val data = MutableLiveData<UpdateProfileResponse?>()

        var apiInterface = ApiClient.getClient(context).create(ApiInterface::class.java)
        apiInterface.updateProfile(map,file).enqueue(object : Callback<UpdateProfileApi?> {
            override fun onResponse(
                call: Call<UpdateProfileApi?>,
                response: Response<UpdateProfileApi?>
            ) {
                if (response.code() == 200) {
                    if (response.body()!!.code == 1||response.body()!!.code == 4||response.body()!!.code == 301) {
                        data.postValue(UpdateProfileResponse(response.body()));
                    }
                    else {
                        data.postValue(UpdateProfileResponse(UnknownError(response.body()!!.data.error)));
                    }
                } else {
                    data.postValue(UpdateProfileResponse(UnknownError(GlobalConstants.ERROR_WENT_WRONG)));
                }
            }
            override fun onFailure(call: Call<UpdateProfileApi?>, t: Throwable) {
                data.postValue(UpdateProfileResponse(t));
            }
        })
        return data
    }

    companion object {
        private var repository: UpdateProfileRepository? = null
        val instance: UpdateProfileRepository?
            get() {
                if (repository == null) {
                    repository =UpdateProfileRepository()
                }
                return repository
            }
    }
}