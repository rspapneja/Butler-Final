package com.butlerschocolates.app.database.dbmodel

data class ProductOptionItem(
    var cartId:Int,
    var product_title: String ,
    var product_id :Int,
    var category_id: Int,
    var product_qty: Int,
    var product_max_qty: Int,
    var subCategoryId: Int,
    var productImage : String,
    var store_id: Int,
    var description :String,
    var storeTitle :String,
    var price: Double,
    var productOptionsSize:Int,
    var cur_symbol :String,
    var optionItem: List<OptionAttribute>,
    var complementaryItemsList: List<ComplementaryItemsDB>
)


