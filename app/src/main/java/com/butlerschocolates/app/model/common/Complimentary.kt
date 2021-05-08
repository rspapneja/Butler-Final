package com.butlerschocolates.app.model.common


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Complimentary(
    @SerializedName("aid")
    val aid: Int=0,
    @SerializedName("image")
    val image: String="",
    @SerializedName("is_multi_select")
    val isMultiSelect: Int=0,
    @SerializedName("is_required")
    val isRequired: Int=0,
    @SerializedName("isoption_expanded")
    val isoptionExpanded: Int=0,
    @SerializedName("items")
    val items: List<Items>?=ArrayList(),
    @SerializedName("max_allowed")
    val maxAllowed: Int=0,
    @SerializedName("restrict_attributes")
    val restrictAttributes: Int=0,
    @SerializedName("title")
    val title: String=""
) : Parcelable