package com.butlerschocolates.app.model.storetiming
import com.google.gson.annotations.SerializedName

class StoreTimingRequestBody(
    @SerializedName("auth_token")
    var authToken: String, // 3ab50ca96ee95be592cb007b1b861488
    @SerializedName("products")
    var productRequestBody: List<ProductRequestBody>,
    @SerializedName("version")
    var version: String, // 1.0
   @SerializedName("store_id")
    var store_id: String // 1.0
   // , var complementaryItem: List<ProductRequestBody.Complementary>
) {
    class ProductRequestBody(
        @SerializedName("options")
        var optionsRequestBody: List<OptionsRequestBody>,
        @SerializedName("product_id")
        var productId: Int, // 1
        @SerializedName("quantity")
        var quantity: Int // 1
    ) {
        class OptionsRequestBody(
            @SerializedName("paid")
            var paid: Int // 5
        )
      /*  class Complementary(
            @SerializedName("id")
            var id: Int
        )*/
    }
}