package com.butlerschocolates.app.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.view.Gravity

import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

import com.butlerschocolates.app.R
import com.butlerschocolates.app.util.Utilities


class SavedCardDialog(context: Context?, savedCardListener: SavedCardListener) {

    var context = context

    var utilities = Utilities(context!!)
    var savedCardListener = savedCardListener

    var dialog:Dialog?=null

    fun showCardDialog() {

        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setContentView(R.layout.popup_saved_cards)
        dialog!!.window?.setGravity(Gravity.CENTER)

        val window: Window = dialog!!.window!!

        val width = (context!!.getResources().getDisplayMetrics().widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog!!.setCanceledOnTouchOutside(false);

        var savedCardRecyclerView = dialog!!.findViewById(R.id.savedCardsList) as RecyclerView
        var closePopup = dialog!!.findViewById(R.id.closedialog) as ImageButton

        closePopup.setOnClickListener {
            dialog!!.dismiss()
        }

        savedCardListener.showSavedCardList(savedCardRecyclerView)
        dialog!!.show()
    }

    fun closeDialog() {
        dialog!!.dismiss()
    }

    interface SavedCardListener {
        fun deleteSavedCard(cardId: Int,remove:Int)
        fun showSavedCardList(recyclerview: RecyclerView)
    }
}