package com.butlerschocolates.app.model.changepass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdatePasswordAfterForgotPassRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("new_password")
    @Expose
    var new_password: String? = null
}