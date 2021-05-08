package com.butlerschocolates.app.viewmodels.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.notification.NotificationApi
import com.butlerschocolates.app.model.notification.NotificationRequestBody

import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

open class NotificationViewModel : BaseViewModel() {

    private var _notificationResponse: LiveData<Resource<NotificationApi>?> = MutableLiveData()

    val notificationResponse: LiveData<Resource<NotificationApi>?>
        get() = _notificationResponse

    fun notificationRequest(requestBody: NotificationRequestBody) = viewModelScope.launch {
        _notificationResponse= appRepository!!.notificationDetail(requestBody)
    }
}