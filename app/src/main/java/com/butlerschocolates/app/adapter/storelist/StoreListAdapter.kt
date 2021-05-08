package com.butlerschocolates.app.adapter.storelist


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerViewStorelistBinding
import com.butlerschocolates.app.model.storelist.Store
import java.util.*


class StoreListAdapter
    (context: FragmentActivity,
    storelist: ArrayList<Store>,
     storeListClickedListener: StoreClickedListener
) : RecyclerView.Adapter<StoreListAdapter.ViewHolder>(){

    val context = context
    var storelist=storelist
    var storeClickedListener = storeListClickedListener

    override fun getItemCount(): Int
    {
        return storelist!!.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.position=position
        holder.binding.storelist = storelist[position]
        holder.binding.storeItemClick=storeClickedListener
        holder.binding.executePendingBindings()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding: RecyclerViewStorelistBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),R.layout.recycler_view_storelist, parent, false)
        return ViewHolder(binding)
    }
    class ViewHolder( val binding: RecyclerViewStorelistBinding) : RecyclerView.ViewHolder(binding.root) {}

    interface StoreClickedListener {
        fun onStoreLocationsSelected(store: Store,pos:Int  )
    }
}