package com.butlerschocolates.app.viewmodels.login

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.model.login.LoginReqestBody
import com.butlerschocolates.app.respostiory.login.LoginApiiResponse
import com.butlerschocolates.app.respostiory.login.LoginRepository
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import com.butlerschocolates.app.viewmodels.syncLoyalty.SyncLoyaltyViewModel

class LoginViewModel : SyncLoyaltyViewModel() {

    private var mutableLiveData: MutableLiveData<LoginApiiResponse?>? = null
    private var repository: LoginRepository? = null

    fun init()
    {
        if (mutableLiveData != null) { return }
        repository = LoginRepository.instance;
     }

    fun loginRequest( requestBody: LoginReqestBody,activity: FragmentActivity): MutableLiveData<LoginApiiResponse?>?
    {
       // setIsLoading(true,activity)
        mutableLiveData = repository!!.login(activity,requestBody)
        return mutableLiveData
    }

    override fun onCleared() {
        super.onCleared()
    }
}