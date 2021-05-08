package com.butlerschocolates.app.model.deleteSavedCard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RemoveSavedCardRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("card_id")
    @Expose
    var card_id: String? = null
}