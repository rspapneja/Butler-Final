package com.butlerschocolates.app.viewmodels.storedetail

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.model.storedetail.StoreDetailRequestBody
import com.butlerschocolates.app.respostiory.storedetail.StoreDetailApiResponse
import com.butlerschocolates.app.respostiory.storedetail.StoreDetailRepository
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel

class StoreDetailViewModel : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<StoreDetailApiResponse?>? = null
    private var storeRepository: StoreDetailRepository? = null

    fun init()
    {
        if (mutableLiveData != null) {
            return
        }
        storeRepository = StoreDetailRepository.instance;
    }

    fun storeDetailApiRequest( storeRequestBody: StoreDetailRequestBody,activity: FragmentActivity): MutableLiveData<StoreDetailApiResponse?>?
    {
        setIsLoading(true,activity)
        mutableLiveData = storeRepository!!.getStoreDetail(activity,storeRequestBody)
        return mutableLiveData
    }

    override fun onCleared() {
        super.onCleared()
    }
}