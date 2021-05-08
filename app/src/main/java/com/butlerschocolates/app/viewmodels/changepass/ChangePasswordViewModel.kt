package com.butlerschocolates.app.viewmodels.changepass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.butlerschocolates.app.api.Resource
import com.butlerschocolates.app.model.changepass.ChangePasswordAPI
import com.butlerschocolates.app.model.changepass.UpdatePasswordAfterForgotPassRequestBody
import com.butlerschocolates.app.model.changepass.UpdatePasswordLoginUserRequestBody
import com.butlerschocolates.app.viewmodels.baseviewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ChangePasswordViewModel : BaseViewModel() {

    private var _changePassResponse: LiveData<Resource<ChangePasswordAPI>?> = MutableLiveData()

    val changePassResponse: LiveData<Resource<ChangePasswordAPI>?>
        get() = _changePassResponse

    fun updatePasswordAfterForgotRequest(requestBody: UpdatePasswordAfterForgotPassRequestBody) = viewModelScope.launch {
        _changePassResponse= appRepository!!.updatePasswordAfterForgot(requestBody)
    }

   fun updatePasswordLoginUser(requestBody: UpdatePasswordLoginUserRequestBody) = viewModelScope.launch {
        _changePassResponse= appRepository!!.updatePasswordForLoginUser(requestBody)
    }
}