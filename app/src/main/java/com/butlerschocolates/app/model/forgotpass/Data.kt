package com.butlerschocolates.app.model.forgotpass



import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


@Parcelize

data class Data(
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("displaytxt")
    val displaytxt: String,
    @SerializedName("error")
    val error: String,

    @SerializedName("success")
    val success: String
) : Parcelable