package com.butlerschocolates.app.adapter.storedetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.callback.OptionsCheckerListerner
import com.butlerschocolates.app.databinding.RecyclerviewMenuLayoutBinding
import com.butlerschocolates.app.model.storedetail.Menu
import com.butlerschocolates.app.util.Utilities

class MenuAdapter(context: Context, menu:ArrayList<Menu>,currencyType:String,optionsCheckerListerner: OptionsCheckerListerner) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){

    val context = context
    val list = menu
    var currencyType=currencyType
    var optionsCheckerListerner=optionsCheckerListerner
    val utilities=Utilities(context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.position=position
        holder.binding.list = list[position]
        holder.binding.curencyType =currencyType
        holder.binding.text =holder.binding.tvProductQty
        holder.binding.optionsCheckerListerner=optionsCheckerListerner
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewMenuLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recyclerview_menu_layout, parent, false)
          return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder( val binding: RecyclerviewMenuLayoutBinding) : RecyclerView.ViewHolder(binding.root) { }
}