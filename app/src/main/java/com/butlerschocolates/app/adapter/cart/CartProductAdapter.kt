package com.butlerschocolates.app.adapter.cart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerProductCartLayoutBinding
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.OptionAttribute
import com.butlerschocolates.app.database.dbmodel.ProductOptionItem
import com.butlerschocolates.app.util.Utilities

class CartProductAdapter(context: Context) :
    RecyclerView.Adapter<CartProductAdapter.ViewHolder>() {

    var context: Context?=null
    var displayProductCartArray: ArrayList<ProductOptionItem>?=null
    var cartProductClickedListener: CartProductClickedListener?=null
    var utilities: Utilities?=null
    var i=1

    constructor(context: Context,  displayProductCartArray: ArrayList<ProductOptionItem>,cartProductClickedListener: CartProductClickedListener): this(context) {
         this.context = context
        this.displayProductCartArray  = displayProductCartArray
        this.cartProductClickedListener = cartProductClickedListener
        this.utilities = Utilities(context)
    }

    constructor(context: Context,  displayProductCartArray: ArrayList<ProductOptionItem>,i:Int,cartProductClickedListener: CartProductClickedListener): this(context) {
        this.context = context
        this.displayProductCartArray  = displayProductCartArray
        this.cartProductClickedListener = cartProductClickedListener
        this.utilities = Utilities(context)
        this.i=i
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvProductName.setText(displayProductCartArray!!.get(position).product_title)
        holder.binding.tvProductQty.setText(displayProductCartArray!!.get(position).product_qty.toString())
        holder.binding.productQty.setText(displayProductCartArray!!.get(position).product_qty.toString())
        holder.binding.tvProductPrice.setText(displayProductCartArray!!.get(position).cur_symbol+String.format("%.2f", displayProductCartArray!!.get(position).price))

        holder.binding.price.setText(
            displayProductCartArray!!.get(position).cur_symbol+
                    String.format("%.2f", (displayProductCartArray!!.get(position).product_qty*( calulateProductPriceAtPostion(position)+displayProductCartArray!!.get(position).price)))
        )

        holder.binding.deleteCartProduct.setOnClickListener {
            cartProductClickedListener!!.deleteCartProduct(position)
        }

        holder.binding.increaseQty.setOnClickListener {
            cartProductClickedListener!!.updateCartProductQty(position,"plus")
        }

        holder.binding.decreaseQty.setOnClickListener {
            cartProductClickedListener!!.updateCartProductQty(position,"minus")
        }

        if(i==1)
        {
            holder.binding.displayQtyLayout.visibility=View.INVISIBLE
            holder.binding.productQtyLayout.visibility= View.VISIBLE
            holder.binding.deleteCartProduct.visibility= View.VISIBLE
        }
        else{
            holder.binding.displayQtyLayout.visibility=View.VISIBLE
            holder.binding.productQtyLayout.visibility= View.GONE
        }

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.binding!!.optionsAttributesRecyclerview!!.setLayoutManager(layoutManager)
        var attributeCartAdapter =
            AttributeCartAdapter( context!!, displayProductCartArray!!.get(position).optionItem!! as ArrayList<OptionAttribute>,displayProductCartArray!!.get(position).cur_symbol )
        holder.binding!!.optionsAttributesRecyclerview!!.setAdapter(attributeCartAdapter)


        var comlementarrytManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.binding!!.comlementaryItemsREcyclerView!!.setLayoutManager(comlementarrytManager)
        var complementaryItemAdpter =
            ComplementaryCartProductAdapter( context!!, displayProductCartArray!!.get(position).complementaryItemsList as ArrayList<ComplementaryItemsDB> )
        holder.binding!!.comlementaryItemsREcyclerView!!.setAdapter(complementaryItemAdpter)

        val comp_item_string = StringBuilder()
        if(displayProductCartArray!!.get(position).complementaryItemsList.size>0)
        {
           for (j in 0..(displayProductCartArray!!.get(position).complementaryItemsList.size - 1)) {
            comp_item_string.append(displayProductCartArray!!.get(position).complementaryItemsList[j].value)
                .append(if(displayProductCartArray!!.get(position).complementaryItemsList.size-1==j) "" else ", " )
           }
            holder.binding!!.complementaryItemName.setText(comp_item_string)
            holder.binding!!.complementaryLayout.visibility=View.VISIBLE
        }
    }

    private fun calulateProductPriceAtPostion(position:Int):Double {

        var optiontotalPrice=0.0

        for( i in 0..displayProductCartArray!!.get(position).optionItem!!.size-1)
          optiontotalPrice=optiontotalPrice+displayProductCartArray!!.get(position).optionItem!!.get(i).option_price

        return  optiontotalPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerProductCartLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_product_cart_layout,
            parent,
            false
        )
        return ViewHolder(
            binding
        )
    }

    class ViewHolder(val binding: RecyclerProductCartLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun getItemCount(): Int {
        return displayProductCartArray!!.size
    }

    interface CartProductClickedListener {
        fun deleteCartProduct( pos:Int)
        fun updateCartProductQty(pos:Int, actionType:String)
    }
}