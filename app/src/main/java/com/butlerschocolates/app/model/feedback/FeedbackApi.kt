package com.butlerschocolates.app.model.feedback


import com.google.gson.annotations.SerializedName

data class FeedbackApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)