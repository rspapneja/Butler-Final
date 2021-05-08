package com.butlerschocolates.app.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.butlerschocolates.app.R
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.Utilities

class LoyaltyCardNumberDialog(
    context: Context,
    loyaltyCardNumberListener: LoyaltyCardNumberListener
) {

    var context = context

    var utilities = Utilities(context!!)
    var loyaltyCardNumberListener = loyaltyCardNumberListener

    var dialog: Dialog? = null

    fun showLoyaltyCardNumberDialog() {

        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setContentView(R.layout.popup_loyalty_card_number)
        dialog!!.window?.setGravity(Gravity.CENTER)

        val window: Window = dialog!!.window!!

        val width = (context!!.resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog!!.setCanceledOnTouchOutside(false)

        val submitCardNumber: Button = dialog!!.findViewById(R.id.submitCardNumber) as Button
        val loyaltyCardNo: EditText = dialog!!.findViewById(R.id.loyaltyCardNo) as EditText
        val closePopup: Button = dialog!!.findViewById(R.id.closePopup) as Button

        submitCardNumber.setOnClickListener {
            if (loyaltyCardNo.text.trim().isNotEmpty()) {
                loyaltyCardNumberListener.onLoyaltyCardNumberSubmitted(loyaltyCardNo.text.toString())
                dialog!!.dismiss()
            } else {
                Console.Toast(context, "Please enter your loyalty card number!!")
            }
        }

        closePopup.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.show()
    }

    interface LoyaltyCardNumberListener {
        fun onLoyaltyCardNumberSubmitted(loyaltyCardNumbder: String)
    }
}