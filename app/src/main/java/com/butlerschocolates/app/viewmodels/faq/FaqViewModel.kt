package com.butlerschocolates.app.viewmodels.faq

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.faq.FaqRequestBady
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyalityCardRequestBody
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyaltyCard
import com.butlerschocolates.app.model.query.AddSubmitQueryApi
import com.butlerschocolates.app.model.query.AddSubmitQueryRequestBody
import com.butlerschocolates.app.respostiory.faq.FaqApiResponse
import com.butlerschocolates.app.respostiory.faq.FaqRepository
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

class FaqViewModel : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<FaqApiResponse?>? = null
    private var repository: FaqRepository? = null

    private var _sendQueryResponse: LiveData<Resource<AddSubmitQueryApi>?> = MutableLiveData()

    fun init()
    {
        if (mutableLiveData != null) { return }
        repository = FaqRepository.instance;
     }

    fun faqApiRequest( requestBody: FaqRequestBady,activity: FragmentActivity): MutableLiveData<FaqApiResponse?>?
    {
        setIsLoading(true,activity)
        mutableLiveData = repository!!.getFaq(activity,requestBody)
        return mutableLiveData
    }

    val sendQueryResponse: LiveData<Resource<AddSubmitQueryApi>?>
        get() = _sendQueryResponse

    fun sendQuery(requestBody: AddSubmitQueryRequestBody) = viewModelScope.launch {
        _sendQueryResponse= appRepository!!.sendQuery(requestBody)
    }

}