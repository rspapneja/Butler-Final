package com.butlerschocolates.app.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R


class MyOrdersHistoryAdapter(context: Context) : RecyclerView.Adapter<MyOrdersHistoryAdapter.ViewHolder>() {

    val context = context


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
    override fun getItemCount(): Int {
        return 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_order_history, parent, false))
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderHistoryStatus = itemView.findViewById(R.id.orderHistoryStatus) as TextView
        val orderHistoryCreated = itemView.findViewById(R.id.orderHistoryCreated) as TextView
    }
}