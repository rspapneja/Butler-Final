package com.butlerschocolates.app.model.storedetail


import android.os.Parcelable
import com.butlerschocolates.app.model.common.Complimentary
import com.butlerschocolates.app.model.common.Option
import com.butlerschocolates.app.model.common.Product
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Menu(
    @SerializedName("isSelected")
    var isSelected:Int=0,
    @SerializedName("subcategory_id")
    var subcategoryId: Int, // 4
    @SerializedName("subcategory_title")
    var subcategoryTitle: String, // Pastries & Scones
    @SerializedName("subcategory_image")
    var subcategoryImage: String,
    @SerializedName("desc")
    var desc: String, // Chocolate Croissant
    @SerializedName("image")
    var image: String, // no-image.png
    @SerializedName("max_quantity")
    var maxQuantity: Int, // 5
    @SerializedName("options")
    var options: List<Option>?=ArrayList(),
    @SerializedName("pid")
    var pid: Int, // 9
    @SerializedName("price")
    var price: Double, // 12
    @SerializedName("products")
    var products: List<Product>?=ArrayList(),
    @SerializedName("qty")
    var qty: Int, // 0
    @SerializedName("title")
    var title: String, // Chocolate Croissant
    @SerializedName("hide_show")
    var hide_show:Boolean=false,
    @SerializedName("complimentary")
    val complimentary: Complimentary?=null ,
    @SerializedName("is_complimentary")
    val is_complimentary: Boolean
    ) : Parcelable