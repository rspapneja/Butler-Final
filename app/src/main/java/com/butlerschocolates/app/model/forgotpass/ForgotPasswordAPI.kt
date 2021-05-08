package com.butlerschocolates.app.model.forgotpass


import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


@Parcelize
data class ForgotPasswordAPI(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
) : Parcelable