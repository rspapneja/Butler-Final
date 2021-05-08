package com.butlerschocolates.app.model.storetiming


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Timing(
    @SerializedName("day")
    var day: String, // Mon
    @SerializedName("time")
    var time: List<String>
) : Parcelable