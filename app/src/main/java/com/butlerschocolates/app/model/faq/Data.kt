package com.butlerschocolates.app.model.faq


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("faqs")
    val faqs: List<Faq>,
    @SerializedName("error")
    val error: String
)