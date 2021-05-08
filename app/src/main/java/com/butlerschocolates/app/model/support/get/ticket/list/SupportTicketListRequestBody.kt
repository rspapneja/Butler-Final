package com.butlerschocolates.app.model.support.get.ticket.list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SupportTicketListRequestBody {
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