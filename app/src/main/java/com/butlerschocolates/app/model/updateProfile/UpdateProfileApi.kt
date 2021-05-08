package com.butlerschocolates.app.model.updateProfile


import com.google.gson.annotations.SerializedName

data class UpdateProfileApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)