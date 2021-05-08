package com.butlerschocolates.app.adapter.support

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerviewSupportOrderListBinding


import com.butlerschocolates.app.model.support.get.orderlist.Order


class SupportOrdersListAdapter(context: Context, list:ArrayList<Order>, listerner:SupportOrderClickedListener) : RecyclerView.Adapter<SupportOrdersListAdapter.ViewHolder>() {

    val context = context
    val list = list
    val listerner = listerner

    override fun getItemCount(): Int
    {
        return list!!.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.position=position
        holder.binding.list = list[position]
        holder.binding.listerner =listerner
        holder.binding.executePendingBindings()

        if(position%2==0)
            holder.binding.layoutParent.setBackgroundColor(context.resources.getColor( R.color.colorSeaBlueDark))
        else
            holder.binding.layoutParent.setBackgroundColor(context.resources.getColor( R.color.colorSeaBlueLight))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewSupportOrderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recyclerview_support_order_list, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder( val binding: RecyclerviewSupportOrderListBinding) : RecyclerView.ViewHolder(binding.root) {}

    interface SupportOrderClickedListener {
        fun onSupportOrderItemSelected(order: Order, pos:Int  )
    }
}