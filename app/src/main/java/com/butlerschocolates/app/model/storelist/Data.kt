package com.butlerschocolates.app.model.storelist


import android.os.Parcelable
import com.butlerschocolates.app.model.common.Loyalty
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    @SerializedName("stores")
    val stores: List<Store>,
    @SerializedName("loyalty")
    var loyalty: Loyalty,
    @SerializedName("error")
    var error:String
): Parcelable