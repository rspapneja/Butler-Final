package com.butlerschocolates.app.model.verifyotp


import com.google.gson.annotations.SerializedName

data class VerifyOtpApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)