package com.butlerschocolates.app.viewmodels.syncLoyalty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.resendotp.OtpRequestBody
import com.butlerschocolates.app.model.resendotp.ResendOtpApi
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyApi
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyRequestBody
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

open class SyncLoyaltyViewModel : BaseViewModel() {

    private var _syncLoyaltyResponse: LiveData<Resource<SyncLoyaltyApi>?> = MutableLiveData()
    private var _resendOtpResponse: LiveData<Resource<ResendOtpApi>?> = MutableLiveData()

    val syncLoyaltyResponse: LiveData<Resource<SyncLoyaltyApi>?>
        get() = _syncLoyaltyResponse

    fun syncLoyaltyRequest(requestBody: SyncLoyaltyRequestBody) = viewModelScope.launch {
        _syncLoyaltyResponse= appRepository!!.syncLoyalty(requestBody)
    }

    val resendOtpResponse: LiveData<Resource<ResendOtpApi>?>
        get() = _resendOtpResponse

    fun resendOtpRequest(requestBody: OtpRequestBody) = viewModelScope.launch {
        _resendOtpResponse= appRepository!!.requestOtp(requestBody)
    }

}