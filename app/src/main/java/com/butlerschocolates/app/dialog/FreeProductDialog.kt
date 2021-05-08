package com.butlerschocolates.app.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.util.Utilities


class FreeProductDialog(context: Context,title: String,message: String,loyaltyFreeValue:Int,freeProductListener: FreeProductListener){
    var context = context

    var utilities = Utilities(context!!)
    var maxFreeProductQuantity=loyaltyFreeValue
    var selectedFreeProductQuantity=1
    var message=message
    var title=title
    var freeProductListener=freeProductListener

    var dialog: Dialog?=null


    fun showFreeProductDialog() {

        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setContentView(R.layout.popup_free_product)
        dialog!!.window?.setGravity(Gravity.CENTER)

        val window: Window = dialog!!.window!!

        val width = (context!!.getResources().getDisplayMetrics().widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog!!.setCanceledOnTouchOutside(false);

        var closePopup = dialog!!.findViewById(R.id.bt_cancel) as Button
        var getFreeProduct = dialog!!.findViewById(R.id.bt_get_it) as Button
        var tv_loyalty_quantity = dialog!!.findViewById(R.id.tv_loyalty_quantity) as TextView
        var tv_messageText = dialog!!.findViewById(R.id.tv_messageText) as TextView
        var plus_quantity = dialog!!.findViewById(R.id.plus_quantity) as ImageView
        var minus_quantity = dialog!!.findViewById(R.id.minus_quantity) as ImageView

        tv_messageText.text=message


        plus_quantity.setOnClickListener {

            if(selectedFreeProductQuantity<maxFreeProductQuantity) {
                selectedFreeProductQuantity++
                tv_loyalty_quantity.text=selectedFreeProductQuantity.toString()
            }
            else{
                utilities!!.showAlert("Alert",title)
            }
        }

        minus_quantity.setOnClickListener {

            if(selectedFreeProductQuantity<=maxFreeProductQuantity && selectedFreeProductQuantity>1) {
                selectedFreeProductQuantity--
                tv_loyalty_quantity.text=selectedFreeProductQuantity.toString()
            }
        }

        closePopup.setOnClickListener {
            dialog!!.dismiss()
        }

        getFreeProduct.setOnClickListener {
            freeProductListener.getFreeProduct(selectedFreeProductQuantity)
            dialog!!.dismiss()
        }

        dialog!!.show()
    }

    interface FreeProductListener
    {
       fun getFreeProduct(freeProductQuantity:Int)
    }


}