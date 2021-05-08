package com.butlerschocolates.app.model.verifyotp


import com.butlerschocolates.app.model.common.Customer
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("customer")
    val customer: Customer,
    @SerializedName("error")
    val error: String,
    @SerializedName("ask_enroll")
    val ask_enroll: String
)