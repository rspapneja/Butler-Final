package com.butlerschocolates.app.adapter.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R

import com.butlerschocolates.app.databinding.RecyclerComplementaryItemLayoutBinding
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB



class ComplementaryCartProductAdapter(context: Context, complementaryItemsList: List<ComplementaryItemsDB> ) : RecyclerView.Adapter<ComplementaryCartProductAdapter.ViewHolder>(){
    val context = context
    val complementaryItemsList = complementaryItemsList

    override fun getItemCount(): Int {
        return complementaryItemsList.size
    }
   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Log.e("value",complementaryItemsList.get(position).value.toString())

       holder.binding.attributeName.setText(" + "+complementaryItemsList.get(position).value.toString())
    }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerComplementaryItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.recycler_complementary_item_layout, parent, false
        )
        return ViewHolder(
            binding
        )
    }
    class ViewHolder(val binding: RecyclerComplementaryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}