package com.butlerschocolates.app.model.support.detail


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Ticket(
    @SerializedName("comments")
    var comments: List<Comment>,
    @SerializedName("created")
    var created: String, // Aug 28 2020 08:30 AM
    @SerializedName("customer_id")
    var customerId: Int, // 13
    @SerializedName("customer_ticket_id")
    var customerTicketId: Int, // 1
    @SerializedName("message")
    var message: String, // Test
    @SerializedName("modified")
    var modified: String, // 2020-10-06 06:43:32
    @SerializedName("order_id")
    var orderId: Int, // 602
    @SerializedName("pikcup_code")
    var pikcupCode: Int, // 0
    @SerializedName("products")
    var products: String, // Americano,Latte
    @SerializedName("status")
    var status: String, // open
    @SerializedName("type")
    var type: String, // order
    @SerializedName("store_logo")
    var store_logo:String

) : Parcelable