package com.butlerschocolates.app.model.verifyotp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyOtpRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("otp")
    @Expose
    var otp: Int? = 0
    @SerializedName("device_id")
    @Expose
    var device_id: String? = null
    @SerializedName("device_type")
    @Expose
    var device_type: String? = "android"
    @SerializedName("cid")
    @Expose
    var cid: String? = null

}