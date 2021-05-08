package com.butlerschocolates.app.model.support.add.comment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddSupportCommentRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("ticket_id")
    @Expose
    var ticket_id: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}