package com.butlerschocolates.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.util.Utilities


class CupAdapter(drawableArray: ArrayList<String>?,loyalty:Loyalty) :
    RecyclerView.Adapter<CupAdapter.MyViewHolder>() {

    var itemView: View? = null
    var drawableArray=drawableArray
    var context:Context?=null
    var loyalty=loyalty

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var personImg: ImageView? = null
        var tv_cup_no: TextView? = null

        init
        {
            personImg = view.findViewById(R.id.img_cup)
            tv_cup_no = view.findViewById(R.id.tv_cup_no)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context=  parent.getContext()
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_cup_adapter, parent, false)
        return MyViewHolder(itemView!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        var utilities=Utilities(context!!);
        utilities.loadImage(drawableArray!!.get(position),holder.personImg!!)

        if((loyalty.totalItems-loyalty.freeItems)>(position))
        holder.tv_cup_no!!.setText(""+(position+1))
    }
    override fun getItemCount(): Int {
        return drawableArray!!.size
    }
}

