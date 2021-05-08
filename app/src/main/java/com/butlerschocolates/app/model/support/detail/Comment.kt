package com.butlerschocolates.app.model.support.detail


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable


@Parcelize
data class Comment(
    @SerializedName("admin_id")
    var adminId: Int, // 0
    @SerializedName("created")
    var created: String, // Aug 28 2020 10:42 AM
    @SerializedName("customer_id")
    var customerId: Int, // 13
    @SerializedName("history_id")
    var historyId: Int, // 1
    @SerializedName("message")
    var message: String, // Reply Test
    @SerializedName("profile_pic")
    var profilePic: String // 1598274471_e2abf6026530a6c2c3a3.jpg
) : Parcelable