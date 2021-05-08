package com.butlerschocolates.app.model.storedetail


import android.annotation.SuppressLint
import android.os.Parcelable
import com.butlerschocolates.app.model.common.Loyalty
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Data(

    @SerializedName("error")
    var error: String,
    @SerializedName("banner_img")
    var bannerImg: String,
    @SerializedName("contact_no")
    var contactNo: String, // 158-144-151
    @SerializedName("email")
    var email: String, // butlers@gmail.com
    @SerializedName("currency_symbol")
    var currency_symbol: String, // £
    @SerializedName("is_expandable")
    var isExpandable: Int, // 1
    @SerializedName("menu")
    var menu: List<Menu>,
    @SerializedName("store_id")
    var storeId: Int, // 1
    @SerializedName("store_title")
    var storeTitle: String, // Butlers Wicklow Street
    @SerializedName("loyalty")
    var loyalty: Loyalty
) : Parcelable