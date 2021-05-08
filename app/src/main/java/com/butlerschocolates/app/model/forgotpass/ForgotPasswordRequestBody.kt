package com.butlerschocolates.app.model.forgotpass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotPasswordRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null

}