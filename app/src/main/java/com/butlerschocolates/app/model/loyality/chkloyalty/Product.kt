package com.butlerschocolates.app.model.loyality.chkloyalty


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Product(
    @SerializedName("price")
    val price: Double = 4.2,
    @SerializedName("product_id")
    val productId: Int = 2
) : Parcelable