package com.butlerschocolates.app.model.storetiming


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.butlerschocolates.app.model.storetiming.Data


@Parcelize
data class StoreTimingAPI(
    @SerializedName("code")
    var code: Int, // 1
    @SerializedName("data")
    var `data`: Data
) : Parcelable