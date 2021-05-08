package com.butlerschocolates.app.viewmodels.butlerCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardApi
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardRequestBody
import com.butlerschocolates.app.model.loyality.LoyalityApi
import com.butlerschocolates.app.model.loyality.LoyalityRequestBody
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyalityCardRequestBody
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyaltyCard
import com.butlerschocolates.app.model.loyality.confirmTopupPayment.ConfirmTopupPaymentApi
import com.butlerschocolates.app.model.loyality.confirmTopupPayment.ConfirmTopupPaymentRequestBody
import com.butlerschocolates.app.viewmodels.syncLoyalty.SyncLoyaltyViewModel
import kotlinx.coroutines.launch

class ButlerCardViewModel : SyncLoyaltyViewModel() {

    private var _loyaltyResponse: LiveData<Resource<LoyalityApi>?> = MutableLiveData()
    private var _addLoyaltyCardResponse: LiveData<Resource<AddLoyaltyCard>?> = MutableLiveData()
    private var _confirmTopupPaymentResponse: LiveData<Resource<ConfirmTopupPaymentApi>?> = MutableLiveData()
    private var _getSavedCardResponse: LiveData<Resource<GetSavedCardApi>?> = MutableLiveData()

    val loyaltyResponse: LiveData<Resource<LoyalityApi>?>
        get() = _loyaltyResponse

    val confirmTopupPaymentResponse: LiveData<Resource<ConfirmTopupPaymentApi>?>
        get() = _confirmTopupPaymentResponse


    val getSavedCardResponse: LiveData<Resource<GetSavedCardApi>?>
        get() = _getSavedCardResponse

    fun loyalty(requestBody: LoyalityRequestBody) = viewModelScope.launch {
        _loyaltyResponse= appRepository!!.loyalty(requestBody)
    }

    fun confirmTopupPayment(requestBody: ConfirmTopupPaymentRequestBody) = viewModelScope.launch {
        _confirmTopupPaymentResponse= appRepository!!.confirmTopupPayment(requestBody)
    }

    fun getSavedCards(requestBody: GetSavedCardRequestBody) = viewModelScope.launch {
        _getSavedCardResponse= appRepository!!.getSavedCards(requestBody)
    }

    val addLoyaltyCardResponse: LiveData<Resource<AddLoyaltyCard>?>
        get() = _addLoyaltyCardResponse

    fun addloyaltyCard(requestBody: AddLoyalityCardRequestBody) = viewModelScope.launch {
        _addLoyaltyCardResponse= appRepository!!.addLoyaltyCard(requestBody)
    }
}