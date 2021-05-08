package com.butlerschocolates.app.model.query

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddSubmitQueryRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("comment")
    @Expose
    var comment: String? = null
}