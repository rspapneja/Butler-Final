package com.butlerschocolates.app.model.getSavedCard


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import com.butlerschocolates.app.model.common.Customer
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.model.storetiming.SavedCards

@Parcelize
data class Data(
    @SerializedName("error")
    val error: String,
    @SerializedName("saved_cards")
    val savedCards: List<SavedCards>,
    @SerializedName("customer")
    val customer: Customer,
    @SerializedName("loyalty_card")
   val loyalty_card: LoyaltyCard,
    @SerializedName("loyalty")
    val loyalty: Loyalty
) : Parcelable