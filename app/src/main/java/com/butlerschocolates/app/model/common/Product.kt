package com.butlerschocolates.app.model.common


import android.annotation.SuppressLint
import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Product(
    @SerializedName("desc")
    var desc: String, // Chocolate Croissant
    @SerializedName("image")
    var image: String, // no-image.png
    @SerializedName("max_quantity")
    var maxQuantity: Int, // 5
    @SerializedName("options")
    var options: List<Option>,
    @SerializedName("complimentary")
    val complimentary: Complimentary? =null,
    @SerializedName("is_complimentary")
    val is_complimentary: Boolean,
    @SerializedName("pid")
    var pid: Int, // 9
    @SerializedName("price")
    var price: Double, // 12
    @SerializedName("qty")
    var qty: Int, // 0
    @SerializedName("title")
    var title: String // Chocolate Croissant
) : Parcelable