package com.butlerschocolates.app.viewmodels.orderlist

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.orderlist.OrderListApI
import com.butlerschocolates.app.model.orderlist.OrderListRequestBody

import com.butlerschocolates.app.respostiory.AppRepository
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

open class OrderListViewModel : BaseViewModel() {
    val repository: AppRepository = AppRepository()

    private var _orderListResponse: LiveData<Resource<OrderListApI>?> = MutableLiveData()

    val orderListResponse: LiveData<Resource<OrderListApI>?>
        get() = _orderListResponse

    fun getOrderList(requestBody: OrderListRequestBody,activity:FragmentActivity) = viewModelScope.launch {
        setIsLoading(true,activity)
        _orderListResponse= repository!!.getOrderList(requestBody)
    }
}