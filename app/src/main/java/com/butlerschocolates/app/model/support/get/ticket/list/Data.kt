package com.butlerschocolates.app.model.support.get.ticket.list


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.butlerschocolates.app.model.support.get.ticket.list.Ticket


@Parcelize
data class Data(
    @SerializedName("error")
    var error: String,
    @SerializedName("is_next")
    var isNext: Boolean, // true
    @SerializedName("tickets")
    var tickets: List<Ticket>
) : Parcelable