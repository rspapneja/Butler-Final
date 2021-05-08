package com.butlerschocolates.app.adapter.support

import android.content.Context
import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerViewSupportTicketListBinding

import com.butlerschocolates.app.model.support.get.ticket.list.Ticket


class SupportTicketListAdapter(context: Context, ticketList:ArrayList<Ticket>,supportTicketClickedListener: SupportTicketClickedListener) : RecyclerView.Adapter<SupportTicketListAdapter.ViewHolder>() {

    val context = context
    val ticketList = ticketList
    val supportTicketClickedListener = supportTicketClickedListener

    override fun getItemCount(): Int {
        return ticketList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        if(ticketList[position].type.equals("order",true))
        {
          holder.binding.supportType.setText((ticketList[position].type+" "+ticketList[position].orderId.toString()).toUpperCase())
        }
        else
        {
          holder.binding.supportType.setText(ticketList[position].type.toUpperCase())
        }

        holder.binding.position=position
        holder.binding.list = ticketList[position]
        holder.binding.supportTicketClickedListener =supportTicketClickedListener
        holder.binding.executePendingBindings()

        if(position%2==0)
            holder.binding.layoutParent.setBackgroundColor(context.resources.getColor( R.color.colorSeaBlueDark))
        else
            holder.binding.layoutParent.setBackgroundColor(context.resources.getColor( R.color.colorSeaBlueLight))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewSupportTicketListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recycler_view_support_ticket_list, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder( val binding: RecyclerViewSupportTicketListBinding) : RecyclerView.ViewHolder(binding.root) {}

    interface SupportTicketClickedListener {
        fun onSupportTicketSelected(ticket: Ticket, pos:Int)
    }
}