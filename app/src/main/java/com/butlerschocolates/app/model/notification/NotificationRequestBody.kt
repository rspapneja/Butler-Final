package com.butlerschocolates.app.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

}