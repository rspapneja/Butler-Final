package com.butlerschocolates.app.model.faq


import com.google.gson.annotations.SerializedName

data class FaqApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)