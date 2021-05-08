package com.butlerschocolates.app.viewmodels.setting


import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.setting.SettingApi
import com.butlerschocolates.app.model.setting.SettingRequestBody
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel : BaseViewModel() {

    private var _settingResponse: LiveData<Resource<SettingApi>?> = MutableLiveData()

    val settingResponse: LiveData<Resource<SettingApi>?>
        get() = _settingResponse

    fun getAndUpdateAppSettingRequest(requestBody: SettingRequestBody, activity: FragmentActivity) = viewModelScope.launch {
        _settingResponse= appRepository!!.getAndUpdateAppSettingDetail(requestBody)
    }
}