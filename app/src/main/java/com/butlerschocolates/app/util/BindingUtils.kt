package com.butlerschocolates.app.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun loadImage(imageView: ImageView, imageURL: String?) {
    if(imageURL!=null) {
        Glide.with(imageView.getContext())
            .load(GlideHeaders.getUrlWithHeaders(AppConstants.BASE_IMAGE_URL + imageURL))
            .into(imageView)
    }
}

@BindingAdapter("text")
fun setText(textView: TextView, st: String){
    if ((st.equals("null",ignoreCase = true)||st.equals(""))) textView.setText("") else textView.setText(st)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("currencySymbol","amount")
fun setText(textView: TextView, currencySymbol: String,amount:Double){
    textView.setText(currencySymbol+ String.format("%.2f",amount))
}

