package com.butlerschocolates.app.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.view.Gravity

import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

import com.butlerschocolates.app.R
import com.butlerschocolates.app.util.Utilities
import com.hbb20.CountryCodePicker


class BillingdAddressDialog(context: Context?, billingAddressListerner: BillingAddressListerner) {

    var context = context

    var utilities = Utilities(context!!)
    var dialog:Dialog?=null
    var billingAddressListerner=billingAddressListerner

    fun showAddressPopup(billing_address:String,billing_city:String,billing_county:String,billing_postal_code:String,actionType:String) {

        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setContentView(R.layout.popup_billing_address)
        dialog!!.window?.setGravity(Gravity.CENTER)

        val window: Window = dialog!!.window!!

        val width = (context!!.getResources().getDisplayMetrics().widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog!!.setCanceledOnTouchOutside(false);

        var closePopup = dialog!!.findViewById(R.id.closedialog) as ImageButton
        var confirmPayButton=dialog!!.findViewById(R.id.bt_confirmaPay) as Button

        var ed_billing_address=dialog!!.findViewById(R.id.ed_billing_address) as EditText
        var city=dialog!!.findViewById(R.id.city) as EditText
        var postalCode=dialog!!.findViewById(R.id.postal_code) as EditText
        var country_code=dialog!!.findViewById(R.id.country_code) as CountryCodePicker

        if(actionType.equals("SavedCard"))
        {
            ed_billing_address.setText(billing_address)
            postalCode.setText(billing_postal_code)
            city.setText(billing_city)
            country_code.setCountryForNameCode(billing_county)
        }

        confirmPayButton.setOnClickListener {
           billingAddressListerner.addBillingAddress(ed_billing_address.text.toString(),
               city.text.toString(),
               country_code.selectedCountryNameCode,
               postalCode.text.toString(),
               actionType)

        }

        closePopup.setOnClickListener {
            closeDialog()
        }

        dialog!!.show()
    }

    fun closeDialog() {
        dialog!!.dismiss()
    }

    interface BillingAddressListerner {
       fun addBillingAddress(billing_address:String,billing_city:String,billing_county:String,billing_postal_code:String,actionType:String)
    }
}