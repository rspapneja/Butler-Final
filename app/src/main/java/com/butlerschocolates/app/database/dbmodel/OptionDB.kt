package com.butlerschocolates.app.database.dbmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OptionDB (
   @SerializedName("option_name")
   var option_name: String ,
   @SerializedName("attribute_id")
   var attribute_id: Int,
   @SerializedName("Options_image")
   var Options_image : String,
   @SerializedName("itemsDB")
   var itemsDB: List<ItemsDB>,
   @SerializedName("restricted_value")
   var restricted_value:Int //new addittion
): Parcelable


