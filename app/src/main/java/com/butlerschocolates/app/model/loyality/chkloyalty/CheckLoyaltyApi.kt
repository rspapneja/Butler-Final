package com.butlerschocolates.app.model.loyality.chkloyalty


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class CheckLoyaltyApi(
    @SerializedName("code")
    val code: Int = 1,
    @SerializedName("data")
    val `data`: Data = Data()
) : Parcelable