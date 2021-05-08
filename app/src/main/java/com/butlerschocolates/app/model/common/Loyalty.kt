package com.butlerschocolates.app.model.common


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Loyalty(
    @SerializedName("filled_item_img")
    var filledItemImg: String, // 1598946045_341dfc47e51f842319b8.png
    @SerializedName("filled_items")
    var filledItems: Int, // 2
    @SerializedName("free_item_img")
    var freeItemImg: String, // 1598946037_db5ae2515898fbf1fd91.png
    @SerializedName("free_items")
    var freeItems: Int, // 1
    @SerializedName("total_item_img")
    var totalItemImg: String, // 1598946030_a5c847403ad77a3e5b0f.png
    @SerializedName("total_items")
    var totalItems: Int // 10
) : Parcelable