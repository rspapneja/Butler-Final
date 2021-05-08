package com.butlerschocolates.app.model.faq


import com.google.gson.annotations.SerializedName

data class Faq(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("question")
    val question: String
)