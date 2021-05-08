package com.butlerschocolates.app.adapter.storedetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.callback.OptionsCheckerListerner
import com.butlerschocolates.app.databinding.RecyclerViewProductBinding
import com.butlerschocolates.app.model.common.Product

class ProductAdapter(context: Context, menuPos:Int, list:ArrayList<Product>, currencyType:String, optionsCheckerListerner: OptionsCheckerListerner) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    val context = context
    val list = list
    val menuPos = menuPos
    var currencyType=currencyType
    var optionsCheckerListerner=optionsCheckerListerner

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.menuPos=menuPos
        holder.binding.productPos=position
        holder.binding.list = list[position]
        holder.binding.curencyType =currencyType
        holder.binding.text =holder.binding.tvProductQty
        holder.binding.optionsCheckerListerner=optionsCheckerListerner
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recycler_view_product, parent, false)

         return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder( val binding: RecyclerViewProductBinding) : RecyclerView.ViewHolder(binding.root) {}
}