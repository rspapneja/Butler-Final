package com.butlerschocolates.app.model.syncloyalty


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.butlerschocolates.app.model.common.Customer


@Parcelize
data class Data(
    @SerializedName("customer")
    val customer: Customer,
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: String
) : Parcelable