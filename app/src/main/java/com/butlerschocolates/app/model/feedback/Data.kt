package com.butlerschocolates.app.model.feedback


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("success")
    val success: String,
    @SerializedName("error")
    val error: String
)