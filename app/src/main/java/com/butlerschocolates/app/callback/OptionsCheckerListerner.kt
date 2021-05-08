package com.butlerschocolates.app.callback

import androidx.appcompat.widget.AppCompatTextView

interface OptionsCheckerListerner {

    fun insertProduct(view: AppCompatTextView, menuPos:Int)
    fun insertProduct(view: AppCompatTextView,menuPos:Int, productPos:Int)
    fun checkStoreType(productQtyView: AppCompatTextView,menuPos:Int, productPos:Int)

}