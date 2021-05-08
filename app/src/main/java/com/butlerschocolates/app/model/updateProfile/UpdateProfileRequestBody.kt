package com.butlerschocolates.app.model.updateProfile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateProfileRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("name")
    @Expose
     var name: String? = null
    @SerializedName("auth_token")
    @Expose
     var auth_token: String? = null
    @SerializedName("country_code")
    @Expose
     var country_code: Int? = 0
    @SerializedName("mobile")
    @Expose
     var mobile:  String? =null
    @SerializedName("password")
    @Expose
     var password: String? = null
    @SerializedName("profilepic")
    @Expose
     var profilepic: Int? = 0
}