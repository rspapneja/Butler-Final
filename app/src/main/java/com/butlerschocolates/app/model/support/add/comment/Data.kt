package com.butlerschocolates.app.model.support.add.comment


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Data(
    @SerializedName("error")
    var error: String,
    @SerializedName("message")
    var message: String, // Ticket generated successfully.
    @SerializedName("ticket_id")
    var ticketId: Int // 19
) : Parcelable