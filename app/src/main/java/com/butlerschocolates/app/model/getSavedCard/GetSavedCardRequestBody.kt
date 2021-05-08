package com.butlerschocolates.app.model.getSavedCard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetSavedCardRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("onlycards")
    @Expose
    var onlycards: Int? = null

}