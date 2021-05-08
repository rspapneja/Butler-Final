package com.butlerschocolates.app.viewmodels.orderInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.ApiHandler

import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.home.HomeApi
import com.butlerschocolates.app.model.home.HomeRequestBody
import com.butlerschocolates.app.model.orderInfo.OrderInfoApi
import com.butlerschocolates.app.model.orderInfo.OrderInfoRequestBody
import com.butlerschocolates.app.model.reorder.ReOrderApi
import com.butlerschocolates.app.model.reorder.ReorderRequestBody
import com.butlerschocolates.app.respostiory.AppRepository

import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch


class OrderInfoViewModel : BaseViewModel() {
    val repository: AppRepository = AppRepository()

    private var _loginResponse: LiveData<Resource<HomeApi>?> = MutableLiveData()

    private var _orderStatusResponse: LiveData<Resource<OrderInfoApi>?> = MutableLiveData()
    private var _reorderResponse: LiveData<Resource<ReOrderApi>?> = MutableLiveData()

    val loginResponse: LiveData<Resource<HomeApi>?>
        get() = _loginResponse

    val orderStatusResponse: LiveData<Resource<OrderInfoApi>?>
        get() = _orderStatusResponse

    val reorderResponse: LiveData<Resource<ReOrderApi>?>
        get() = _reorderResponse

    fun getOrderStatusInfo(requestBody: OrderInfoRequestBody) = viewModelScope.launch {

        _orderStatusResponse=repository!!.getOrderStatusInfo(requestBody)
    }

    fun reOrderRequest(requestBody: ReorderRequestBody) = viewModelScope.launch {
        _reorderResponse=repository!!.reOrder(requestBody)
    }

    fun onHomeRequest(
        homeRequestBody: HomeRequestBody
    ) = viewModelScope.launch {
        _loginResponse= repository!!.getHomeResponse1(homeRequestBody)
    }

}