package com.butlerschocolates.app.model.payment


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Redirect(
    @SerializedName("message")
    var message: String, // Please redirect your customer to the ACSURL to complete the 3DS Transaction
    @SerializedName("order_id")
    var orderId: Int, // 348
    @SerializedName("redirect_url")
    var redirectUrl: String // https://butlers.dev.qkangaroo.com/portal/ipn/webview?t=vX6z9z/CAboggqLtxzFsoLEsKBEO2sjIpkdsBiNGym3PWdsCDcn39cYZpGD6YeYpjZnCwGmu7SYYJUv2cFUlST9axImNwKIxstWL83WbTOTiio0J1kWNl3HAvS3Dzpw2UuE4AutgPEHizPmgyMjqUNumoSvo8WH1wA==
) : Parcelable