package com.butlerschocolates.app.model.loyality


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Redirect(
    @SerializedName("message")
    val message: String,
    @SerializedName("payment_id")
    val paymentId: Int,
    @SerializedName("redirect_url")
    val redirectUrl: String
) : Parcelable