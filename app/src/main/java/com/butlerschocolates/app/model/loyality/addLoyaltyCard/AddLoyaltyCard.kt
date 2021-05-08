package com.butlerschocolates.app.model.loyality.addLoyaltyCard


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class AddLoyaltyCard(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
) : Parcelable