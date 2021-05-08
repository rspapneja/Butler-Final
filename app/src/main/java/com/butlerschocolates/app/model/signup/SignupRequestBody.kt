package com.butlerschocolates.app.model.signup

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignupRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("name")
    @Expose
     var name: String? = null
    @SerializedName("email")
    @Expose
     var email: String? = null
    @SerializedName("country_code")
    @Expose
     var country_code: Int? = 0
    @SerializedName("mobile")
    @Expose
     var mobile:  String? =null
    @SerializedName("password")
    @Expose
     var password: String? = null
    @SerializedName("is_terms_req")
    @Expose
     var is_terms_req: Int? = 0
}