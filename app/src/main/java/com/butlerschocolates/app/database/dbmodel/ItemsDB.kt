package com.butlerschocolates.app.database.dbmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemsDB (
    @SerializedName("patt_id")
  var patt_id: Int ,
    @SerializedName("option_value")
   var option_value : String,
    @SerializedName("option_price")
   var option_price: Double
): Parcelable

