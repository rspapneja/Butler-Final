package com.butlerschocolates.app.adapter.myorderlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerViewMyorderListBinding
import com.butlerschocolates.app.model.orderlist.Succes


class MyOrdersListAdapter(context: Context,list:ArrayList<Succes>,listerner:OrderListClickedListener) : RecyclerView.Adapter<MyOrdersListAdapter.ViewHolder>() {

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
        val binding: RecyclerViewMyorderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recycler_view_myorder_list, parent, false)
        return ViewHolder(binding)
    }
    class ViewHolder( val binding: RecyclerViewMyorderListBinding) : RecyclerView.ViewHolder(binding.root) {}

    interface OrderListClickedListener {
        fun onOrderItemSelected(order: Succes, pos:Int  )
    }
}