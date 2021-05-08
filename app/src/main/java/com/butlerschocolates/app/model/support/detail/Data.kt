package com.butlerschocolates.app.model.support.detail


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("error")
    var error: String,
    @SerializedName("ticket")
    var ticket: Ticket
) : Parcelable