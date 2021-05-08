package com.butlerschocolates.app.database.dbmodel

data class ComplementaryItemsDB (
  var cart_id: Int ,
  var product_id: Int ,
  var complementary_info_id: Int ,
  var aid: Int ,
   var value : String,
   var value_id : Int,
   var image: String
)

