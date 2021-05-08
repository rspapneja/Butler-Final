package com.butlerschocolates.app.model.storelist


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Category(
    @SerializedName("isSelected")
    var isSelected:Int=0,
    @SerializedName("category_id")
    var categoryId: Int, // 1
    @SerializedName("category_image")
    var categoryImage: String, // 1596609856_4511221862cf7bd5ed09.png
    @SerializedName("category_title")
    var categoryTitle: String // Hot Drinks
) : Parcelable