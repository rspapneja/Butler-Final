package com.butlerschocolates.app.adapter.customizeyourproduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerviewOptionsLayoutBinding
import com.butlerschocolates.app.model.common.Item
import com.butlerschocolates.app.model.common.Option
import com.butlerschocolates.app.util.Utilities


class OptionsAdapter(context: Context, option: ArrayList<Option>, currencyType: String) :
    RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    val context = context
    val option = option
    var currencyType = currencyType

    var utilities = Utilities(context)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        utilities.loadImage(option.get(position).image, holder.binding.optionsImg)
        holder.binding.tvOptionTitle.setText(option[position].title)

        if (option.get(position).hide_show) {
            holder.binding!!.recyclerViewItems!!.visibility = View.VISIBLE
            holder.binding!!.imgHide.setRotation(180F)
        }
        else {
            holder.binding!!.recyclerViewItems!!.visibility = View.GONE
            holder.binding!!.imgHide.setRotation(0F)
        }

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.binding!!.recyclerViewItems!!.setLayoutManager(layoutManager)

        var itemsAdapter =
            ItemsAdapter(
                context,
                option.get(position).items as ArrayList<Item>,
                currencyType,option.get(position).isMultiSelect
            )
        holder.binding!!.recyclerViewItems!!.setAdapter(itemsAdapter)

        holder.binding.root.setOnClickListener {
            for (i in 0..option.size - 1) {
                if (position == i) {
                    if (option.get(i).hide_show)
                        option.get(i).hide_show = false
                    else
                        option.get(i).hide_show = true
                } else {
                    option.get(i).hide_show = false
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewOptionsLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.recyclerview_options_layout, parent, false
        )
        return ViewHolder(
            binding
        )
    }

    class ViewHolder(val binding: RecyclerviewOptionsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun getItemCount(): Int {
        return option.size
    }
}