package com.butlerschocolates.app.model.storetiming


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class SavedCards(
    @SerializedName("card_mask")
    var card_mask: String,
    @SerializedName("card_id")
    var card_id: String,
    @SerializedName("isSelected")
    var isSelected: Int,
    @SerializedName("billing")
    var billing: BillingAddress

) : Parcelable