package com.butlerschocolates.app.model.storedetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StoreDetailRequestBody {
    @SerializedName("version")
    @Expose
    var version: String? = null
    @SerializedName("store_id")
    @Expose
    var store_id: String? = null
    @SerializedName("category_id")
    @Expose
    var category_id: String? = null
    @SerializedName("auth_token")
    @Expose
    var auth_token: String? = null
}