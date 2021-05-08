package com.butlerschocolates.app.model.orderInfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderInfoRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("webview_return")
    @Expose
    var webview_return: String? = null
    @SerializedName("order_id")
    @Expose
    var order_id: Int? = 0
    @SerializedName("check_payment")
    @Expose
    var check_payment:  Int? = 0
}