package com.butlerschocolates.app.model.loyality.chkloyalty


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class RedeemLoyalty(
    @SerializedName("discount")
    val discount: Double = 7.9,
    @SerializedName("product")
    val product: List<Product> = listOf()
) : Parcelable