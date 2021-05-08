package com.butlerschocolates.app.model.logout


import com.google.gson.annotations.SerializedName

data class LogoutApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)