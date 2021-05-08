package com.butlerschocolates.app.viewmodels.savecard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.deleteSavedCard.DeleteSavedCard
import com.butlerschocolates.app.model.deleteSavedCard.RemoveSavedCardRequestBody
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardApi
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardRequestBody
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

open class SavedCardViewModel :BaseViewModel()
{
  private var _getSavedCardResponse: LiveData<Resource<GetSavedCardApi>?> = MutableLiveData()
  private var _removeSavedCardResponse: LiveData<Resource<DeleteSavedCard>?> = MutableLiveData()

   val getSavedCardResponse: LiveData<Resource<GetSavedCardApi>?>
        get() = _getSavedCardResponse

    val removeSavedCardResponse: LiveData<Resource<DeleteSavedCard>?>
        get() = _removeSavedCardResponse

    fun getSavedCards(requestBody: GetSavedCardRequestBody) = viewModelScope.launch {
        _getSavedCardResponse= appRepository!!.getSavedCards(requestBody)
    }

    fun removeSavedCard(requestBody: RemoveSavedCardRequestBody) = viewModelScope.launch {
        _removeSavedCardResponse= appRepository!!.removeSavedCard(requestBody)
    }
}
