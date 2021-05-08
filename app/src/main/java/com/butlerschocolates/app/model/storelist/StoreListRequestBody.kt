package com.butlerschocolates.app.model.storelist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StoreListRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
}