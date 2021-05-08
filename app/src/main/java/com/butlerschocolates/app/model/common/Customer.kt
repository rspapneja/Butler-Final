package com.butlerschocolates.app.model.common


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    @SerializedName("auth_token")
    val authToken: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("currency_code")
    val currencyCode: String,
    @SerializedName("currency_id")
    val currencyId: String,
    @SerializedName("currency_symbol")
    val currencySymbol: String,
    @SerializedName("customer_id")
    val customerId: String,
    @SerializedName("customer_name")
    val customerName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("identity_key")
    val identityKey: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("profile_pic")
    val profilePic: String,
    @SerializedName("wallet_amount")
    val wallet_amount: Double,
    @SerializedName("sync_with_butlers")
    val syncWithButlers: Int=0,
    @SerializedName("loginfrom")
    val loginfrom: String
) : Parcelable