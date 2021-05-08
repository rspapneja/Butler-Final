package com.butlerschocolates.app.viewmodels.feedback

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.model.feedback.FeedbackRequestBody
import com.butlerschocolates.app.respostiory.feedback.FeedbackApiResponse
import com.butlerschocolates.app.respostiory.feedback.FeedbackRepository
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel

class FeedbackViewModel : BaseViewModel() {

    private var mutableLiveData: MutableLiveData<FeedbackApiResponse?>? = null
    private var repository: FeedbackRepository? = null

    fun init()
    {
        if (mutableLiveData != null) { return }
        repository = FeedbackRepository.instance;
     }

    fun sendFeedbackRequest( requestBody: FeedbackRequestBody,activity: FragmentActivity): MutableLiveData<FeedbackApiResponse?>?
    {
       setIsLoading(true,activity)
        mutableLiveData = repository!!.sendFeedback(activity,requestBody)
        return mutableLiveData
    }
    override fun onCleared() {
        super.onCleared()
    }
}