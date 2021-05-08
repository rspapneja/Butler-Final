package com.butlerschocolates.app.model.payment


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.butlerschocolates.app.model.common.Customer

@SuppressLint("ParcelCreator")
@Parcelize
data class Data(
    @SerializedName("error")
    var error: String, // Store is not available at the moment. Please try after some time.
    @SerializedName("redirect")
    var redirect: Redirect,
    @SerializedName("success")
    var success: Success,
    @SerializedName("customer")
    var customer: Customer
) : Parcelable