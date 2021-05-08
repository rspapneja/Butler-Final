package com.butlerschocolates.app.adapter.savedCard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerCardLayoutBinding
import com.butlerschocolates.app.model.storetiming.SavedCards

class SavedCardAdapter(
    context: Context,

    list: ArrayList<SavedCards>,
    savedCardClickedListener: SavedCardAdapter.SavedCardClickedListener
) : RecyclerView.Adapter<SavedCardAdapter.ViewHolder>() {

    val context = context
    val list = list
    var selectedSavedCardPos = 0
    var savedCardClickedListener = savedCardClickedListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.position = position
        holder.binding.list = list[position]
        holder.binding.savedCardClickedListener = savedCardClickedListener
        holder.binding.executePendingBindings()

        if (list.get(position).isSelected == 1) {
            selectedSavedCardPos=position
            holder.binding!!.checkbox.setBackgroundResource(R.drawable.ic_card_select)
            savedCardClickedListener.onSavedCardSelected(position)
        } else {
            holder.binding!!.checkbox.setBackgroundResource(R.drawable.ic_card_unselect)
        }

        holder.binding.root.setOnClickListener {

            if(selectedSavedCardPos!=position) {
                list.get(position).isSelected = 1
                list.get(selectedSavedCardPos).isSelected = 0
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerCardLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.recycler_card_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: RecyclerCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    interface SavedCardClickedListener {
        fun onSavedCardSelected(pos: Int)

    }
}