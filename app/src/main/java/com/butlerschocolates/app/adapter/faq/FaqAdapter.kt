package com.butlerschocolates.app.adapter.faq

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.RecyclerViewFaqBinding
import com.butlerschocolates.app.model.faq.Faq


class FaqAdapter(context: Context, faqList: List<Faq>) :
    RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    val context = context
    val list = faqList

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 2 == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              holder.binding.linearMain.setBackgroundColor(context.getColor(R.color.colorSeaBlueDark))
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 holder.binding.linearMain.setBackgroundColor(context.getColor(R.color.colorSeaBlueLight))
            }
        }

        holder.binding.faqAnswer.text=list.get(position).answer
        holder.binding.faqQuestion.text=list.get(position).question
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerViewFaqBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.recycler_view_faq, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder( val binding: RecyclerViewFaqBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.faqQuestion.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    binding.faqAnswer.visibility = View.VISIBLE
                else
                    binding.faqAnswer.visibility = View.GONE
            }
        }
    }
}