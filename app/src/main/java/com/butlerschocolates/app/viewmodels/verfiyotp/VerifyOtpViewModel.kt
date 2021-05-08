package com.butlerschocolates.app.viewmodels.verfiyotp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.model.verifyotp.VerifyOtpRequestBody
import com.butlerschocolates.app.respostiory.verifyotp.VerfiyOtpApiResponse
import com.butlerschocolates.app.respostiory.verifyotp.VerifyOtpRepository
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import com.butlerschocolates.app.viewmodels.syncLoyalty.SyncLoyaltyViewModel

class VerifyOtpViewModel : SyncLoyaltyViewModel() {

    private var mutableLiveData: MutableLiveData<VerfiyOtpApiResponse?>? = null
    private var repository: VerifyOtpRepository? = null

    fun init() {
        if (mutableLiveData != null) {
            return
        }
        repository = VerifyOtpRepository.instance;
    }

    fun verifyOtpRequest(context: Context,requestBody: VerifyOtpRequestBody): MutableLiveData<VerfiyOtpApiResponse?>? {
        setIsLoading(true)
        mutableLiveData = repository!!.verifyOtp(context,requestBody)
        return mutableLiveData
    }

    override fun onCleared() {
        super.onCleared()
    }
}