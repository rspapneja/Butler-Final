package com.butlerschocolates.app.adapter.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerAttrtbuteLayoutBinding
import com.butlerschocolates.app.database.dbmodel.OptionAttribute

class AttributeCartAdapter(context: Context,optionAttribute:  ArrayList<OptionAttribute>, currency_symbol :String ) : RecyclerView.Adapter<AttributeCartAdapter.ViewHolder>(){
    val context = context
    val optionAttribute = optionAttribute
    val currency_symbol = currency_symbol

    override fun getItemCount(): Int {
        return optionAttribute.size
    }
   override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       Log.e("value",optionAttribute.get(position).option_value.toString())
       holder.binding.attributePrice.setText(currency_symbol+String.format("%.2f", optionAttribute.get(position).option_price))
       holder.binding.attributeName.setText(" + "+optionAttribute.get(position).option_value)
    }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerAttrtbuteLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.recycler_attrtbute_layout, parent, false
        )
        return ViewHolder(
            binding
        )
    }
    class ViewHolder(val binding: RecyclerAttrtbuteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}