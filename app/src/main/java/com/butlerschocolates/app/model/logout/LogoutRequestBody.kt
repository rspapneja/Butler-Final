package com.butlerschocolates.app.model.logout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LogoutRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
}