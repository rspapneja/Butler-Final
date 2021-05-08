package com.butlerschocolates.app.adapter.complementaryproducts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerviewComplmentaryPrdouctsBinding
import com.butlerschocolates.app.model.common.Items


class ComplementaryProductsAdapter(context: Context, list:ArrayList<Items>,listerner:ComplementaryListener) : RecyclerView.Adapter<ComplementaryProductsAdapter.ViewHolder>(){

    val context = context
    val list = list
    val listerner = listerner

    var isMultiSelect=1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list.get(position).isSelected==0)
            holder.binding.addPrdouctImage.setBackgroundResource(R.drawable.ic_plus)
        else
            holder.binding.addPrdouctImage.setBackgroundResource(R.drawable.ic_tick)

        holder.binding.position=position

        holder.binding.itemList = list[position]

        holder.binding.executePendingBindings()

        holder.binding.parentLayout.setOnClickListener {
            listerner.onItemSelected(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewComplmentaryPrdouctsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recyclerview_complmentary_prdoucts, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder( val binding: RecyclerviewComplmentaryPrdouctsBinding) : RecyclerView.ViewHolder(binding.root) {}

    interface ComplementaryListener {
        fun onItemSelected(pos:Int)
    }
}