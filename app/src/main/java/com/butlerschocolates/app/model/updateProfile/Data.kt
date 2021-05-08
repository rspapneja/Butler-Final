package com.butlerschocolates.app.model.updateProfile


import com.butlerschocolates.app.model.common.Customer
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("customer")
    val customer: Customer,
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: String
)