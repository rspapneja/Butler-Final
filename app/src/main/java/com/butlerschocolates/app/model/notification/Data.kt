package com.butlerschocolates.app.model.notification


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("body")
    val body: String,
    @SerializedName("error")
    val error: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("ityp")
    val ityp: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("title")
    val title: String
) : Parcelable