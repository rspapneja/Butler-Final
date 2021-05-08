package com.butlerschocolates.app.model.payment


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Success(
    @SerializedName("order_id")
    var orderId: Int, // 302
    @SerializedName("order_status")
    var orderStatus: String, // Processing
    @SerializedName("order_title")
    var orderTitle: String, // Your order is ready to pick up from Butlers Wicklow Street
    @SerializedName("pickup_time")
    var pickupTime: String, // Sep 17 2020
    @SerializedName("pikcup_code")
    var pikcupCode: Int // 3021
) : Parcelable