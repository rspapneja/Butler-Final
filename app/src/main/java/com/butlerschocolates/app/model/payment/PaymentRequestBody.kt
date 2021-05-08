package com.butlerschocolates.app.model.payment


import com.google.gson.annotations.SerializedName

class PaymentRequestBody(
    @SerializedName("auth_token")
    var authToken: String,
    @SerializedName("store_id")
    var store_id:String,
    @SerializedName("card_expiry")
    var cardExpiry: String, // 1225
    @SerializedName("card_holder")
    var cardHolder: String, // gulshan
    @SerializedName("card_id")
    var cardId: Int, // 0
    @SerializedName("card_no")
    var cardNo: String, // 4929000000006
    @SerializedName("cvv")
    var cvv: String, // 123
    @SerializedName("is_save_card")
    var isSaveCard: Int, // 1
    @SerializedName("pickup_day")
    var pickupDay: String, // Today
    @SerializedName("pickup_time")
    var pickupTime: String, // 15:10
    @SerializedName("products")
    var products: List<ProductRequestBody>,
    @SerializedName("version")
    var version: String,
    @SerializedName("pay_mode")
    var pay_mode: String,
    @SerializedName("redeem_no")
    var redeemNo : Int,
    @SerializedName("bill_address")
    var bill_address: String,
    @SerializedName("bill_country")
    var bill_country: String,
    @SerializedName("bill_city")
    var bill_city: String,
    @SerializedName("bill_postalCode")
    var bill_postalCode: String
    ) {
    class ProductRequestBody(
        @SerializedName("amount")
        var amount: Double, // 1.20
        @SerializedName("options")
        var options: List<OptionsRequestBody>,
        @SerializedName("product_id")
        var productId: Int, // 1
        @SerializedName("quantity")
        var quantity: Int, // 1,
        @SerializedName("complimentary")
        var complementaryItem: List<Complementary>
    ) {
        class OptionsRequestBody(
            @SerializedName("paid")
            var attributeId: Int // 5
        )
        class Complementary(
            @SerializedName("id")
            var id: Int
        )
    }
}