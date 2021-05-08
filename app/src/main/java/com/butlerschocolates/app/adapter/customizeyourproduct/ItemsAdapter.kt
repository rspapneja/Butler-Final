package com.butlerschocolates.app.adapter.customizeyourproduct

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerviewItemsBinding
import com.butlerschocolates.app.model.common.Item


class ItemsAdapter(context: Context, list:ArrayList<Item>, currencyType:String, isMultiSelect:Int) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>(){

    val context = context
    val list = list
    var currencyType=currencyType
    var isMultiSelect=isMultiSelect


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list.get(position).isSlected==0)
            holder.binding.addPrdouctImage.setBackgroundResource(R.drawable.ic_plus)
        else
            holder.binding.addPrdouctImage.setBackgroundResource(R.drawable.ic_tick)

        holder.binding.position=position
        holder.binding.currencyType=currencyType
        holder.binding.itemList = list[position]

        holder.binding.executePendingBindings()

        holder.binding.parentLayout.setOnClickListener {
            if(isMultiSelect==1) {
                if (list.get(position).isSlected == 0) {
                    list.get(position).isSlected = 1
                } else {
                    list.get(position).isSlected = 0
                }
            }else{
                for (i in 0..list.size - 1) {
                    if (position == i) {
                        if (list.get(i).isSlected==0)
                            list.get(i).isSlected = 1
                        else
                            list.get(i).isSlected = 0
                    } else {
                        list.get(i).isSlected = 0
                    }
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recyclerview_items, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder( val binding: RecyclerviewItemsBinding) : RecyclerView.ViewHolder(binding.root) {}

    interface ItemsClickedListener {
        fun onItemSelected(item: Item, pos:Int)
    }
}