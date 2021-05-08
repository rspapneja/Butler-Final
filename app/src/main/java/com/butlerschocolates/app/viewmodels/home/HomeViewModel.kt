package com.butlerschocolates.app.viewmodels.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.butlerschocolates.app.model.home.HomeRequestBody
import com.butlerschocolates.app.respostiory.home.HomeApiResponse
import com.butlerschocolates.app.respostiory.home.HomeRepository

class HomeViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<HomeApiResponse?>? = null
    private var loaderLiveData: MutableLiveData<Boolean>? = null
    private var homeRepository: HomeRepository? = null

    fun init()
    {
        if (mutableLiveData != null) {
            return
        }
        homeRepository = HomeRepository.instance;
      loaderLiveData=homeRepository!!.loader
    }

    fun onHomeRequest(context: Context, homeRequestBody: HomeRequestBody): MutableLiveData<HomeApiResponse?>?
    {
        mutableLiveData = homeRepository!!.getHomeResponse(context,homeRequestBody)
        return mutableLiveData
    }

     val progressbarObservable: MutableLiveData<Boolean>?
        get() = loaderLiveData


    override fun onCleared() {
        super.onCleared()
    }
}