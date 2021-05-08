package com.butlerschocolates.app.model.loyality.addLoyaltyCard


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("ask_enroll")
    val askEnroll: String,
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: String
) : Parcelable