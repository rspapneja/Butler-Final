package com.butlerschocolates.app.model.query


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class AddSubmitQueryApi(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
) : Parcelable