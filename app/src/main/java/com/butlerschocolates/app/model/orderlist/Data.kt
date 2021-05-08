package com.butlerschocolates.app.model.orderlist


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Data(
    @SerializedName("error")
    var error: String, // Order yet not placed.
    @SerializedName("is_next")
    var isNext: Boolean, // true
    @SerializedName("success")
    var success: List<Succes>
) : Parcelable