package com.butlerschocolates.app.model.support.get.orderlist


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Order(
    @SerializedName("amount")
    var amount: Double, // 6.5
    @SerializedName("created")
    var created: String, // Sep 30 2020 14:04 PM
    @SerializedName("order_id")
    var orderId: Int, // 572
    @SerializedName("products")
    var products: String, // Mocha,Black Tea
    @SerializedName("store_address")
    var storeAddress: String, // T2
    @SerializedName("store_title")
    var storeTitle: String, // Butlers T2
    @SerializedName("currency")
    var currency: String, // T2
    @SerializedName("store_logo")
    var storeLogo: String // Butlers T2
) : Parcelable