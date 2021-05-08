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

import com.butlerschocolates.app.R
import com.butlerschocolates.app.util.Utilities


class SubmitQueryDialog(context: Context?, submitQuery: SubmitQuery) {

    var context = context

    var utilities = Utilities(context!!)
    var submitQuery = submitQuery

    var dialog:Dialog?=null
    fun showSupportDialog() {

        dialog = Dialog(context!!,R.style.CustomAlertDialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setContentView(R.layout.popup_support_query)
        dialog!!.window?.setGravity(Gravity.CENTER)
        val window: Window = dialog!!.window!!
        val width = (context!!.getResources().getDisplayMetrics().widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val submitTicket: Button = dialog!!.findViewById(R.id.submitTicket) as Button
        val ticketText: EditText = dialog!!.findViewById(R.id.ticketText) as EditText
        val closePopup: Button = dialog!!.findViewById(R.id.closePopup) as Button

        closePopup.setOnClickListener {
            dialog!!.dismiss()
        }

        submitTicket.setOnClickListener {
            if (ticketText.text.trim().isNotEmpty()) {
                dialog!!.dismiss()
                submitQuery.onSubmitQuery(ticketText.text.toString())
            } else {
                utilities!!.showAlert("Enter","Please add your support query")
            }
        }

        dialog!!.show()
    }

    interface SubmitQuery {
        fun onSubmitQuery(query: String)
    }
}