package com.butlerschocolates.app.model.login


import com.google.gson.annotations.SerializedName

data class LoginApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)