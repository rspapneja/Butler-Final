package com.butlerschocolates.app.model.payment


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PaymentApi(
    @SerializedName("code")
    var code: Int, // 6
    @SerializedName("data")
    var `data`: Data
) : Parcelable