package com.butlerschocolates.app.model.common


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Item(
    @SerializedName("isslected")
    var isSlected:Int=0,
    @SerializedName("pattid")
    var pattid: Int, // 10
    @SerializedName("price")
    var price: Double, // 0
    @SerializedName("value")
    var value: String // Caramel
) : Parcelable