package com.butlerschocolates.app.model.common


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Items(
    @SerializedName("isSelected")
    var isSelected:Int=0,
    @SerializedName("value")
    val value: String,
    @SerializedName("value_id")
    val valueId: Int,
    @SerializedName("image")
    val image: String
) : Parcelable