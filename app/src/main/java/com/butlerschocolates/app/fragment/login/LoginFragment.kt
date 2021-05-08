package com.butlerschocolates.app.fragment.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.callback.FragmentToActivityCallback
import com.butlerschocolates.app.databinding.FragmentLoginBinding
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation


class LoginFragment : BaseFragment() {

    var validation: Validation? = null

    var binding: FragmentLoginBinding?=null

    var fragmentToActivityCallback: FragmentToActivityCallback? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            fragmentToActivityCallback = activity as FragmentToActivityCallback
        } catch (e: Exception) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false)

        val view: View = binding!!.getRoot()

        viewInitailze()

        checkRememberMe()

        binding!!.btSubmit.setOnClickListener {
            if(validation!!.loginValidate(binding!!.edEmail.text.toString(),binding!!.edPassword.text.toString()))
            fragmentToActivityCallback!!.loginCallback("app",binding!!.edEmail.text.toString(),binding!!.edPassword.text.toString(),binding!!.chkBoxRememberMe!!.isChecked);
        }

        binding!!.btFacebook.setOnClickListener {
            fragmentToActivityCallback!!.loginCallback("facebook","","",false);
        }

        binding!!.tvForgotPassword.setOnClickListener {

       Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_forgetPasswordFragment, null)
       }
        return view
    }

    private fun viewInitailze() {
        utilities = Utilities(requireActivity())
        validation = Validation(requireActivity(), utilities!!)
    }

   /*
   * @method
   * -check remember me
   * */
    fun checkRememberMe()
   {
        if (utilities!!.getRememberPref("rememberMe").trim().isNotEmpty()) {
            if (utilities!!.getRememberPref("rememberMe").equals("true", true)) {
                binding!!.edEmail.setText(utilities!!.getRememberPref("email"))
                binding!!.edPassword.setText(utilities!!.getRememberPref("password"))
            }
        }
    }

    override fun onDetach() {
        fragmentToActivityCallback = null
        super.onDetach()
    }
}
