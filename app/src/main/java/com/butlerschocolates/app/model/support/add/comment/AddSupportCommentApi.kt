package com.butlerschocolates.app.model.support.add.comment


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class AddSupportCommentApi(
    @SerializedName("code")
    var code: Int, // 1
    @SerializedName("data")
    var `data`: Data
) : Parcelable