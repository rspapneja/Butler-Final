package com.butlerschocolates.app.model.login


import com.butlerschocolates.app.model.common.Customer
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("customer")
    val customer: Customer,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: String,
    @SerializedName("customer_id")
    val customer_id: Int,
    @SerializedName("displaytxt")
    val displaytxt: String
)