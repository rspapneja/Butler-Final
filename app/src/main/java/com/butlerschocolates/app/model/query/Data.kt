package com.butlerschocolates.app.model.query


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: String
) : Parcelable