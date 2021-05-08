package com.butlerschocolates.app.model.orderlist


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Succes(
    @SerializedName("amount")
    var amount: Double, // 6.8
    @SerializedName("created")
    var created: String, // Sep 25 2020 09:10
    @SerializedName("currency_symbol")
    var currencySymbol: String, // £
    @SerializedName("invoice_no")
    var invoiceNo: Int, // 431
    @SerializedName("order_id")
    var orderId: Int, // 431
    @SerializedName("order_status")
    var orderStatus: String, // Processing
    @SerializedName("pickup_time")
    var pickupTime: String, // Oct 01 2020 11:25
    @SerializedName("pikcup_code")
    var pikcupCode: String, // 4312 
    @SerializedName("store_logo")
    var storeLogo: String // no-image.png
) : Parcelable