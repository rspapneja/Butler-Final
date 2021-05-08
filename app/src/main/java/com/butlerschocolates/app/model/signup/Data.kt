package com.butlerschocolates.app.model.signup


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("phonetxt")
    val phonetxt: String,
    @SerializedName("success")
    val success: String
)