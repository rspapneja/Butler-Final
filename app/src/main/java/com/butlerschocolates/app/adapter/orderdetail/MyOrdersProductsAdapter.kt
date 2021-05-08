package com.butlerschocolates.app.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerViewOrderProductBinding
import com.butlerschocolates.app.model.common.Product
import java.util.*


class MyOrdersProductsAdapter (context: Context,product:ArrayList<Product>,currency_symbol:String) : RecyclerView.Adapter<MyOrdersProductsAdapter.ViewHolder>() {

    val TAG="Tag MyOrdersProductsAdapter"

    val context = context
    val product = product
    val currency_symbol = currency_symbol

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       val sb = StringBuilder()
       val price_string = StringBuilder()
       val comp_item_string = StringBuilder()
       for (j in 0..(product[position].options!!.size - 1)) {
          for (k in 0..(product[position].options[j].items.size - 1)) {
               sb.append("+"+product[position].options[j].title+":"+product[position]!!.options[j].items[k].value).append("\n")
              price_string.append(currency_symbol+String.format("%.2f",product[position].options[j].items[k].price)).append("\n")
            }
        }
        if(product[position].is_complimentary) {
            for (j in 0..(product[position].complimentary!!.items!!.size - 1)) {
                comp_item_string.append("+" + product[position].complimentary!!.items!![j].value)
                    .append("\n")
            }
        }

        holder.binding.product = product[position]
        holder.binding.itemString =sb.toString()
        holder.binding.priceString =price_string.toString()
        holder.binding.complemebtaryItemString =comp_item_string.toString()
        holder.binding.currencySymbol =currency_symbol
        holder.binding.executePendingBindings()
   }

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewOrderProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recycler_view_order_product, parent, false)

        return ViewHolder(binding)
    }

    class ViewHolder( val binding: RecyclerViewOrderProductBinding) : RecyclerView.ViewHolder(binding.root) {}
}