package com.butlerschocolates.app.fragment.verifydetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.BootStrapProcessActivity
import com.butlerschocolates.app.databinding.FragmentLoginBinding
import com.butlerschocolates.app.databinding.FragmentVerifyEmailPhoneBinding
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation

class VerifyEmailPhoneFragment : Fragment() {

    var validation: Validation? = null
    var utilities: Utilities? = null
    var binding: FragmentVerifyEmailPhoneBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_verify_email_phone, container, false
        )
        val view: View = binding!!.getRoot()

        utilities = Utilities(requireActivity())
        validation = Validation(requireActivity(), utilities!!)

        binding!!.btSubmit.setOnClickListener {

            var b=validation!!.validateVerifyEmailPhone(
                binding!!.edEmail.text.toString(),
                binding!!.countryCodePicker.selectedCountryCode,
                binding!!.edPhone.text.toString()
            )

            if(b)
            {
                (activity as BootStrapProcessActivity).fblogin( binding!!.countryCodePicker.selectedCountryCode,
                    binding!!.edEmail.text.toString(),
                    binding!!.edPhone.text.toString())
            }
        }
        return view
    }
}