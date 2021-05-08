package com.butlerschocolates.app.model.reorder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReorderRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("order_id")
    @Expose
    var order_id: Int? = 0
}