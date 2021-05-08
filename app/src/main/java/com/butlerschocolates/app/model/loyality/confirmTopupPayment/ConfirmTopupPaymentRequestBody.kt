package com.butlerschocolates.app.model.loyality.confirmTopupPayment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfirmTopupPaymentRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("webview_return")
    @Expose
    var webview_return: String? = null
    @SerializedName("pid")
    @Expose
    var pid: Int? = null
}