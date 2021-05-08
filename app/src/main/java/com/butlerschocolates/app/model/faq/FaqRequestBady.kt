package com.butlerschocolates.app.model.faq

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FaqRequestBady {
    @SerializedName("version")
    @Expose
    var version: String? = null

}