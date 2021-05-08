package com.butlerschocolates.app.model.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HomeRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    var auth_token: String? = null

}