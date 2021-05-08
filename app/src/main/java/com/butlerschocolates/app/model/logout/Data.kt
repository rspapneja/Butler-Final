package com.butlerschocolates.app.model.logout


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: String
)