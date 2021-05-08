package com.butlerschocolates.app.model.support.get.ticket.list


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class GetTicketListApi(
    @SerializedName("code")
    var code: Int, // 1
    @SerializedName("data")
    var `data`: Data
) : Parcelable