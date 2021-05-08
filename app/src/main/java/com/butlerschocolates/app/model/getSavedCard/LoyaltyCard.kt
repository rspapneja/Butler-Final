package com.butlerschocolates.app.model.getSavedCard

import android.os.Parcelable
import com.butlerschocolates.app.model.common.Customer
import com.butlerschocolates.app.model.storetiming.SavedCards
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoyaltyCard(
    @SerializedName("card_id")
    val card_id: String,
    @SerializedName("card_mask")
    val card_mask: String
) : Parcelable