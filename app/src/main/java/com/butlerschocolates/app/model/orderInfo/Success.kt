package com.butlerschocolates.app.model.orderInfo


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.butlerschocolates.app.model.common.Product

@Parcelize
data class Success(
    @SerializedName("amount")
    var amount: Double,
    @SerializedName("invoice_no")
    var invoiceNo: Int,
    @SerializedName("order_id")
    var orderId: Int,
    @SerializedName("order_status")
    var orderStatus: String="",
    @SerializedName("order_status_id")
    var orderStatusId: Int, // 7
    @SerializedName("order_status_info")
    var orderStatusInfo: String,
    @SerializedName("payment_method")
    var paymentMethod: String="",
    @SerializedName("pickup_time")
    var pickupTime: String="",
    @SerializedName("pikcup_code")
    var pikcupCode: String="",
    @SerializedName("products")
    var products: List<Product>,
    @SerializedName("store_close_time")
    var storeCloseTime: String="",
    @SerializedName("store_logo")
    var storeLogo: String,
    @SerializedName("store_open_time")
    var storeOpenTime: String="",
    @SerializedName("store_title")
    var storeTitle: String="",
    @SerializedName("store_timing")
    var store_timing: String="",
    @SerializedName("currency_symbol")
    var currency_symbol: String="",
    @SerializedName("created")
    var created: String="",
    @SerializedName("store_id")
    var store_id: Int

) : Parcelable