package com.butlerschocolates.app.model.loyality.addLoyaltyCard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddLoyalityCardRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null

    @SerializedName("card_no")
    @Expose
    var card_no: String? = null

}