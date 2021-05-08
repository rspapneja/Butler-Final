package com.butlerschocolates.app.viewmodels.signup

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.butlerschocolates.app.model.signup.SignupRequestBody
import com.butlerschocolates.app.respostiory.home.SignupRepository
import com.butlerschocolates.app.respostiory.signup.SignUpApiResponse

class SignupViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<SignUpApiResponse?>? = null
    private var loaderLiveData: MutableLiveData<Boolean>? = null
    private var repository: SignupRepository? = null

    fun init()
    {
        if (mutableLiveData != null) {
            return
        }
        repository = SignupRepository.instance;
        loaderLiveData=repository!!.loader
    }

    fun doSignUpRequest(context: Context, requestBody: SignupRequestBody): MutableLiveData<SignUpApiResponse?>?
    {
        mutableLiveData = repository!!.getApiResponse(context,requestBody)
        return mutableLiveData
    }

    val progressbarObservable: MutableLiveData<Boolean>?
        get() = loaderLiveData

    override fun onCleared() {
        super.onCleared()
    }
}