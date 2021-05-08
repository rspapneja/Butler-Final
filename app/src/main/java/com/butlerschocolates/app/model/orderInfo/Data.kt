package com.butlerschocolates.app.model.orderInfo


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("error")
    var error: String, // Order not found.
    @SerializedName("success")
    var success: Success
) : Parcelable