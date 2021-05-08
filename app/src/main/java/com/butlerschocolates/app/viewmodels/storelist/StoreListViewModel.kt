package com.butlerschocolates.app.viewmodels.storelist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.butlerschocolates.app.model.storelist.StoreListRequestBody
import com.butlerschocolates.app.respostiory.storelist.StoreListApiResponse
import com.butlerschocolates.app.respostiory.storelist.StoreListRepository

class StoreListViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<StoreListApiResponse?>? = null
    private var loaderLiveData: MutableLiveData<Boolean>? = null
    private var storeRepository: StoreListRepository? = null

    fun init()
    {
        if (mutableLiveData != null) {
            return
        }
        storeRepository = StoreListRepository.instance;
        loaderLiveData=storeRepository!!.loader
    }

    fun onStoreListApiRequest( context: Context,storeListRequestBody: StoreListRequestBody): MutableLiveData<StoreListApiResponse?>?
    {
        mutableLiveData = storeRepository!!.getStoreList(context,storeListRequestBody)
        return mutableLiveData
    }

    val progressbarObservable: MutableLiveData<Boolean>?
        get() = loaderLiveData

}