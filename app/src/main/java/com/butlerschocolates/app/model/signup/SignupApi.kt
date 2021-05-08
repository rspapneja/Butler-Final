package com.butlerschocolates.app.model.signup


import com.google.gson.annotations.SerializedName

data class SignupApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)