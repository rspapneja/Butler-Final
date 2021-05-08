package com.butlerschocolates.app.model.storelist


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreListApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
): Parcelable