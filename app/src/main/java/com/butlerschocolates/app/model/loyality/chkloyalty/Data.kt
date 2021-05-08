package com.butlerschocolates.app.model.loyality.chkloyalty


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("error")
    val error: String = "The redeem field is required.",
    @SerializedName("loyalty_free")
    val loyaltyFree: Int = 2,
    @SerializedName("loyalty_popup_msg")
    val loyaltyPopupMsg: String = "Want to get FREE cup(s) of coffee? You can redeem up to 2 cup(s) for free into your cart.",
    @SerializedName("loyalty_section_title")
    val loyaltySectionTitle: String = "You are eligible to get 2 cup(s) of coffee for FREE.",
    @SerializedName("redeem_loyalty")
    val redeemLoyalty: RedeemLoyalty = RedeemLoyalty()
) : Parcelable