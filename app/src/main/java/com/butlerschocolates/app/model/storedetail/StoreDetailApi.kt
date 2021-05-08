package com.butlerschocolates.app.model.storedetail


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class StoreDetailApi(
    @SerializedName("code")
    var code: Int, // 1
    @SerializedName("data")
    var `data`: Data
) : Parcelable