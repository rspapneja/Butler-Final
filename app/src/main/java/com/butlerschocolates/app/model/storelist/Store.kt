package com.butlerschocolates.app.model.storelist


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Store(
    @SerializedName("location")
    val location: String,
    @SerializedName("store_id")
    val storeId: String,
    @SerializedName("title")
    val title: String,

    @SerializedName("category")
    var category: List<Category>
): Parcelable