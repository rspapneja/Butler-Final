package com.butlerschocolates.app.model.support.get.orderlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SupportOrderListRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("page")
    @Expose
    var page: Int? = 0
}