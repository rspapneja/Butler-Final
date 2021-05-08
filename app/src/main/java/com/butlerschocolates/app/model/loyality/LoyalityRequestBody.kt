package com.butlerschocolates.app.model.loyality

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoyalityRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
    @SerializedName("is_save_card")
    @Expose
    var is_save_card: Int? = null
    @SerializedName("card_id")
    @Expose
    var card_id: String? = null
    @SerializedName("cvv")
    @Expose
    var cvv: String? = null
    @SerializedName("card_holder")
    @Expose
    var card_holder: String? = null
    @SerializedName("card_no")
    @Expose
    var card_no: String? = null
    @SerializedName("card_expiry")
    @Expose
    var card_expiry: String? = null
    @SerializedName("amount")
    @Expose
    var amount: Double? = 00.00
    @SerializedName("bill_address")
    @Expose
    var bill_address: String? = null
    @SerializedName("bill_country")
    @Expose
    var bill_country: String? = null
    @SerializedName("bill_city")
    @Expose
    var bill_city: String? = null
    @SerializedName("bill_postalCode")
    @Expose
    var bill_postalCode: String? = null
}