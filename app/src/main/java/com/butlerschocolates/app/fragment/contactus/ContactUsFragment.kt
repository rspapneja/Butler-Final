package com.butlerschocolates.app.fragment.contactus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil

import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentContactUsBinding
import com.butlerschocolates.app.global.GlobalConstants


class ContactUsFragment : BaseFragment() {
    var binding: FragmentContactUsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false)
        val view: View = binding!!.getRoot()

        binding!!.tvEmail.setText(utilities!!.getHomeApiData().app_email)
        binding!!.tvAddress.setText(utilities!!.getHomeApiData().app_address)
        binding!!.tvMobile.setText(utilities!!.getHomeApiData().app_mobile)

        return  view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<TextView>(R.id.txt_title).setText(GlobalConstants.ScreenTitle)
    }
}
