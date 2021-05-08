package com.butlerschocolates.app.database.dbmodel

data class ComplementaryProductItem(
    var cartId:Int,
    var product_id :Int,
    var aid: Int,
    var title: String,
    var is_multi_select: Int,
    var restrict_attributes : Int,
    var is_required: Int,
    var isoption_expanded: Int,
    var image:String,
    var complementaryItemsList: List<ComplementaryItemsDB>
)




