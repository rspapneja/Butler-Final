package com.butlerschocolates.app.model.storetiming


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Data(
    @SerializedName("saved_cards")
    var savedCards: List<SavedCards>,
    @SerializedName("timings")
    var timings: List<Timing>,
    @SerializedName("error")
    var error: String,
    @SerializedName("banner_img")
    var banner_img: String,
    @SerializedName("banner_title")
    var banner_title : String,
    @SerializedName("loyalty_free")
    var loyalty_free: Int,
    @SerializedName("loyalty_section_title")
    var loyalty_section_title: String,
   @SerializedName("loyalty_popup_msg")
   var loyalty_popup_msg: String

) : Parcelable