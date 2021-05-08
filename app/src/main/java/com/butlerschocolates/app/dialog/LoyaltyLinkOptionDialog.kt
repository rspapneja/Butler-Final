package com.butlerschocolates.app.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.fragment.card.MyButtlerCardFragment
import com.butlerschocolates.app.util.Utilities

class LoyaltyLinkOptionDialog(context: Context, loyaltyLinkOptionListener: LoyaltyLinkOptionListener) {

    var context = context

    var utilities = Utilities(context!!)
    var chooseLinkLoyaltyListener = loyaltyLinkOptionListener

    var dialog: Dialog?=null

    fun showLoyaltyLinkOptions() {

        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setContentView(R.layout.popup_loyalty_link_option)
        dialog!!.window?.setGravity(Gravity.CENTER)

        val window: Window = dialog!!.window!!

        val width = (context!!.getResources().getDisplayMetrics().widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog!!.setCanceledOnTouchOutside(false)

        var closePopup = dialog!!.findViewById(R.id.closedialog) as ImageButton
        var manualEnterTxt = dialog!!.findViewById(R.id.enterManualTxt) as TextView
        var scanQrCode = dialog!!.findViewById(R.id.scanQrCode) as TextView

        manualEnterTxt.setOnClickListener {
            chooseLinkLoyaltyListener.onLinkOptionSelected(0)
            dialog!!.dismiss()
        }
        scanQrCode.setOnClickListener {
            chooseLinkLoyaltyListener.onLinkOptionSelected(1)
            dialog!!.dismiss()
        }

        closePopup.setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.show()
    }

    interface LoyaltyLinkOptionListener {
        //linkType 0 = manual, 1 = qr scan
        fun onLinkOptionSelected(linkType:Int)
    }
}