package com.butlerschocolates.app.model.resendotp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OtpRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("cid")
    @Expose
    var customerId: Int? = null
    @SerializedName("otptype")
    @Expose
    var OtpType: String? = null
}