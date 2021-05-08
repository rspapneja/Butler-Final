package com.butlerschocolates.app.model.support.get.ticket.list


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Ticket(
    @SerializedName("created")
    var created: String, // Aug 28 2020 08:30 AM
    @SerializedName("customer_name")
    var customerName: String, // Mohit
    @SerializedName("message")
    var message: String, // Test
    @SerializedName("order_id")
    var orderId: Int, // 602
    @SerializedName("products")
    var products: String, // Americano,Latte
    @SerializedName("status")
    var status: String, // open
    @SerializedName("store_logo")
    var storeLogo: String, // no-image.png
    @SerializedName("ticket_id")
    var ticketId: Int, // 1
    @SerializedName("total_comments")
    var totalComments: Int, // 1
    @SerializedName("type")
    var type: String // order
) : Parcelable