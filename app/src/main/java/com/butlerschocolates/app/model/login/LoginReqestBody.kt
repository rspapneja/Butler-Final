package com.butlerschocolates.app.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginReqestBody {

    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("login_type")
    @Expose
    var login_type: String? = null
    @SerializedName("country_code")
    @Expose
    var country_code: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("facebook_id")
    @Expose
    var facebook_id: String? = null
    @SerializedName("customer_name")
    @Expose
    var customer_name: String? = null
    @SerializedName("device_id")
    @Expose
    var device_id: String? = null
    @SerializedName("device_type")
    @Expose
    var device_type: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null


}