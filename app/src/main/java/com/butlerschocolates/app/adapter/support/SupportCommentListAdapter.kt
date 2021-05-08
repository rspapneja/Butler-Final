package com.butlerschocolates.app.adapter.support

import android.content.Context
import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerViewSupportMessageBinding

import com.butlerschocolates.app.model.support.detail.Comment


class SupportCommentListAdapter(context: Context, commentlist:ArrayList<Comment>) : RecyclerView.Adapter<SupportCommentListAdapter.ViewHolder>() {

    val context = context
    val commentlist = commentlist

    override fun getItemCount(): Int {
        return commentlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.position=position
        holder.binding.list = commentlist[position]
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewSupportMessageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recycler_view_support_message, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder( val binding: RecyclerViewSupportMessageBinding) : RecyclerView.ViewHolder(binding.root) {}
}