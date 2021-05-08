package com.butlerschocolates.app.model.support.add.ticket

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddSupportTicketRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("order_id")
    @Expose
    var order_id: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
}