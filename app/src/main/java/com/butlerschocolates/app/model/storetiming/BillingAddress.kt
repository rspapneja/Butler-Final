package com.butlerschocolates.app.model.storetiming


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class BillingAddress(
    @SerializedName("address")
    var address: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("postalcode")
    var postalcode: String,
    @SerializedName("country")
    var country: String
) : Parcelable