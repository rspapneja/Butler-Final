package com.butlerschocolates.app.model.support.get.orderlist


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("error")
    var error: String,
    @SerializedName("is_next")
    var isNext: Boolean, // true
    @SerializedName("orders")
    var orders: List<Order>
) : Parcelable