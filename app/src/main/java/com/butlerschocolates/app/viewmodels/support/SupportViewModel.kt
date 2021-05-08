package com.butlerschocolates.app.viewmodels.support


import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.support.add.comment.AddSupportCommentApi
import com.butlerschocolates.app.model.support.add.comment.AddSupportCommentRequestBody
import com.butlerschocolates.app.model.support.add.ticket.AddSupportTicketApi
import com.butlerschocolates.app.model.support.add.ticket.AddSupportTicketRequestBody
import com.butlerschocolates.app.model.support.detail.SupportTicketDetailApi
import com.butlerschocolates.app.model.support.detail.SupportTicketDetailRequestBody
import com.butlerschocolates.app.model.support.get.orderlist.SupportOrderListApi
import com.butlerschocolates.app.model.support.get.orderlist.SupportOrderListRequestBody
import com.butlerschocolates.app.model.support.get.ticket.list.GetTicketListApi
import com.butlerschocolates.app.model.support.get.ticket.list.SupportTicketListRequestBody
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch


class SupportViewModel : BaseViewModel() {

    private var _supportOrderListResponse: LiveData<Resource<SupportOrderListApi>?> = MutableLiveData()
    private var _addSupportTicketResponse: LiveData<Resource<AddSupportTicketApi>?> = MutableLiveData()
    private var _supportTicketListResponse: LiveData<Resource<GetTicketListApi>?> = MutableLiveData()
    private var _addSupportCommentResponse: LiveData<Resource<AddSupportCommentApi>?> = MutableLiveData()
    private var _supportTicketDetailResponse: LiveData<Resource<SupportTicketDetailApi>?> = MutableLiveData()

    val supportOrderListResponse: LiveData<Resource<SupportOrderListApi>?>
        get() = _supportOrderListResponse

    val addSupportTicketResponse: LiveData<Resource<AddSupportTicketApi>?>
        get() = _addSupportTicketResponse

    val supportTicketListResponse: LiveData<Resource<GetTicketListApi>?>
        get() = _supportTicketListResponse

    val addSupportCommentResponse: LiveData<Resource<AddSupportCommentApi>?>
        get() = _addSupportCommentResponse

    val supportTicketDetailResponse: LiveData<Resource<SupportTicketDetailApi>?>
        get() = _supportTicketDetailResponse

    fun getSupportOrderList(requestBody: SupportOrderListRequestBody, activity: FragmentActivity) = viewModelScope.launch {
        setIsLoading(true,activity)
        _supportOrderListResponse= appRepository!!.getSupportOrderList(requestBody)
    }

    fun addSupportTicket(requestBody: AddSupportTicketRequestBody, activity: FragmentActivity) = viewModelScope.launch {
        setIsLoading(true,activity)
        _addSupportTicketResponse= appRepository!!.addSupportTicket(requestBody)
    }

    fun getSupportTicketList(requestBody: SupportTicketListRequestBody, activity: FragmentActivity) = viewModelScope.launch {
        setIsLoading(true,activity)
        _supportTicketListResponse= appRepository!!.getSupportTicketList(requestBody)
    }

    fun addSupportComment(requestBody: AddSupportCommentRequestBody, activity: FragmentActivity) = viewModelScope.launch {
        setIsLoading(true,activity)
        _addSupportCommentResponse= appRepository!!.addSupportComment(requestBody)
    }

    fun getSupportTicketDetail(requestBody: SupportTicketDetailRequestBody, activity: FragmentActivity,isSupportCommentApiCallFirstTime:Boolean) = viewModelScope.launch {
     if(isSupportCommentApiCallFirstTime) setIsLoading(true,activity)
        _supportTicketDetailResponse= appRepository!!.getSupportTicketDetail(requestBody)
    }
}