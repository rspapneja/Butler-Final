package com.butlerschocolates.app.adapter.getSavedCard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerViewCardsBinding
import com.butlerschocolates.app.dialog.SavedCardDialog
import com.butlerschocolates.app.model.storetiming.SavedCards

class SavedCardsAdapter(context: Context, cardlist:  ArrayList<SavedCards>, savedCardlisterner:SavedCardDialog.SavedCardListener ) : RecyclerView.Adapter<SavedCardsAdapter.ViewHolder>(){
    val context = context
    val cardlist = cardlist
    val savedCardlisterner = savedCardlisterner

    override fun getItemCount(): Int {
        return cardlist.size
    }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
   holder.binding.cardNumber.setText(cardlist.get(position).card_mask)

       holder.binding.deleteCard.setOnClickListener {
          savedCardlisterner.deleteSavedCard(cardlist.get(position).card_id.toInt(),position)
       }
   }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewCardsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.recycler_view_cards, parent, false
        )
        return ViewHolder(
            binding
        )
    }
    class ViewHolder(val binding: RecyclerViewCardsBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}