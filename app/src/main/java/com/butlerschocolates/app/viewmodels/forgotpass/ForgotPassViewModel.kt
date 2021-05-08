package com.butlerschocolates.app.viewmodels.forgotpass


import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.forgotpass.ForgotPasswordAPI
import com.butlerschocolates.app.model.forgotpass.ForgotPasswordRequestBody
import com.butlerschocolates.app.model.setting.SettingApi
import com.butlerschocolates.app.model.setting.SettingRequestBody
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ForgotPassViewModel : BaseViewModel() {

    private var _forgotPassResponse: LiveData<Resource<ForgotPasswordAPI>?> = MutableLiveData()

    val forgotPassResponse: LiveData<Resource<ForgotPasswordAPI>?>
        get() = _forgotPassResponse

    fun forgotPasswordRequest(requestBody: ForgotPasswordRequestBody) = viewModelScope.launch {
        _forgotPassResponse= appRepository!!.forgotPass(requestBody)
    }
}