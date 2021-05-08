package com.butlerschocolates.app.adapter.storedetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerviewCategoryLayoutBinding
import com.butlerschocolates.app.model.storelist.Category
import com.butlerschocolates.app.util.Utilities

class CategoryAdapter(context: Context,selectedCategoryPostion:Int,storeCategoryList:ArrayList<Category>,categorySelectedListerner: CategorySelectedListerner) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    val context = context
    var storeCategoryList=storeCategoryList
    var categorySelectedListerner=categorySelectedListerner
    var selectedCategoryPostion=0
    var utilities: Utilities?= Utilities(context)

    override fun getItemCount(): Int
    {
        return storeCategoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.categorylist = storeCategoryList[position]
        holder.binding.executePendingBindings()

        if(storeCategoryList.get(position).isSelected==1) {
          selectedCategoryPostion=position
          categorySelectedListerner.onCategorySelected(position)
          holder.binding.categoryImage.setBackgroundResource(R.drawable.circular_white_shape)
        }
        else
          holder.binding.categoryImage.setBackgroundResource(R.drawable.circular_skyblue)

       holder.binding!!.categoryParentLayout.setOnClickListener {
           if(selectedCategoryPostion!=position) {
               storeCategoryList.get(position).isSelected = 1
               storeCategoryList.get(selectedCategoryPostion).isSelected = 0
               notifyDataSetChanged()
           }
       }

        if(storeCategoryList.size==1){

            val width = (context.getResources().getDisplayMetrics().widthPixels).toInt()
            val params: ViewGroup.LayoutParams =  holder.binding!!.categoryParentLayout.layoutParams
            params.width = width
            holder.binding!!.categoryParentLayout.layoutParams = params
            holder.binding!!.categoryImage.setPadding(0, 0, 0, 0)


        }else if(storeCategoryList.size==2){

           /* val width = (context.getResources().getDisplayMetrics().widthPixels)/2
            val params: ViewGroup.LayoutParams =  holder.binding!!.categoryParentLayout.layoutParams
            params.width = width
            holder.binding!!.categoryParentLayout.layoutParams = params
            holder.binding!!.categoryImage.setPadding(0, 0, 0, 0)*/

            val width = ((context.getResources().getDisplayMetrics().widthPixels)/2.2).toInt()
            val params: ViewGroup.LayoutParams = holder.binding!!.categoryParentLayout.layoutParams
            params.width = width
            holder.binding!!.categoryParentLayout.layoutParams = params

          }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewCategoryLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_category_layout, parent, false)
        return ViewHolder(binding)
    }
    class ViewHolder( val binding: RecyclerviewCategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

   }

    interface CategorySelectedListerner
    {
        fun onCategorySelected(position: Int)
    }
}