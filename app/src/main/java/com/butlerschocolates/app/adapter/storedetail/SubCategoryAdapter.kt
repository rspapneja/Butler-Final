package com.butlerschocolates.app.adapter.storedetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.callback.OptionsCheckerListerner
import com.butlerschocolates.app.databinding.RecyclerviewSubcategoryLayoutBinding
import com.butlerschocolates.app.model.storedetail.Menu
import com.butlerschocolates.app.model.common.Product
import com.butlerschocolates.app.util.Utilities


class SubCategoryAdapter(context: Context, menu: ArrayList<Menu>, currencyType: String,optionsCheckerListerner: OptionsCheckerListerner) :
    RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    val context = context
    val menu = menu
    var currencyType = currencyType
    var optionsCheckerListerner = optionsCheckerListerner

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var utilities = Utilities(context)
        utilities.loadImage(menu.get(position).subcategoryImage, holder.binding.imgSubcategory)
        holder.binding.tvSubcategoryTitle.setText(menu[position].subcategoryTitle)

        if (menu.get(position).isSelected == 1) {
            holder.binding!!.recyclerViewProduct!!.visibility = View.VISIBLE
            holder.binding!!.imgHide.setRotation(180F)
         }
        else {
           holder.binding!!.recyclerViewProduct!!.visibility = View.GONE
            holder.binding!!.imgHide.setRotation(0F)
        }

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.binding!!.recyclerViewProduct.setLayoutManager(layoutManager)
        var productAdapter =
            ProductAdapter(context,position, menu.get(position).products as ArrayList<Product>, currencyType,optionsCheckerListerner)
        holder.binding!!.recyclerViewProduct.setAdapter(productAdapter)

        holder.binding.subcategoryLayout.setOnClickListener {

            for (i in 0..menu.size - 1) {
                if (position == i) {
                    if (menu.get(i).isSelected == 1)
                        menu.get(i).isSelected = 0
                    else
                        menu.get(i).isSelected = 1
                } else
                    menu.get(i).isSelected = 0
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewSubcategoryLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.recyclerview_subcategory_layout, parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    class ViewHolder(val binding: RecyclerviewSubcategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}