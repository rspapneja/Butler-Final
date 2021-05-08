package com.butlerschocolates.app.model.common


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Option(
    @SerializedName("aid")
    var aid:Int,
    @SerializedName("image")
    var image: String, // no-image.png
    @SerializedName("is_multi_select")
    var isMultiSelect: Int, // 0
    @SerializedName("is_required")
    var isRequired: Int, // 0
    @SerializedName("items")
    var items: List<Item>,
    @SerializedName("restrict_attributes")
    var restrictAttributes: Int, // 0
    @SerializedName("title")
    var title: String, // Flavour
    @SerializedName("hide_show")
    var hide_show:Boolean=false
) : Parcelable