package com.butlerschocolates.app.model.reorder


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.butlerschocolates.app.model.common.Product


@Parcelize
data class Data(
    @SerializedName("error")
    var error: String, // Order not found.
    @SerializedName("products")
    var products: List<Product>
) : Parcelable