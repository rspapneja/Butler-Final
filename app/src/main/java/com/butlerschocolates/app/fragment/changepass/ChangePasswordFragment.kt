package com.butlerschocolates.app.fragment.changepass

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.BootStrapProcessActivity
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentChangePasswordBinding
import com.butlerschocolates.app.model.changepass.UpdatePasswordAfterForgotPassRequestBody
import com.butlerschocolates.app.model.changepass.UpdatePasswordLoginUserRequestBody

import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation
import com.butlerschocolates.app.viewmodels.changepass.ChangePasswordViewModel


class ChangePasswordFragment : BaseFragment() {

    var binding: FragmentChangePasswordBinding? = null
    val settingViewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
    // Inflate the layout for this fragment
     this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
     val view: View = binding!!.getRoot()

      setUpViewModel()

      getArgData()


      binding!!.btSubmit.setOnClickListener {
          val validation=Validation(requireContext(),utilities!!)

          if(requireArguments().getString("changePassRequestType").equals("NotloginUser")) {
            binding!!.currentPassLayout.visibility=View.GONE
             if (validation.changePasswordAfterForgotValidate( binding!!.edNewPass.text.toString(),
                     binding!!.edConfirmPass.text.toString()))

                  callUpdatePasswordAfterForgotPass()
          }
          else{

              if (validation.changePasswordForLoginValidate(
                      binding!!.edOldPass.text.toString(),
                      binding!!.edNewPass.text.toString(),
                      binding!!.edConfirmPass.text.toString()
                  )
              )
                  callUpdatePasswordForLoginUserAPI()
           }
      }
        return view
    }

    private fun getArgData() {
        if(!requireArguments().getString("changePassRequestType").equals("NotloginUser")) {
            (activity as MainActivity).bottomNavigationView!!.visibility=View.GONE
            (activity as MainActivity).top_layout!!.visibility=View.GONE
            binding!!.logoImage.visibility = View.VISIBLE
        }
        else
        {
            binding!!.currentPassLayout!!.visibility=View.GONE
        }
    }

    private fun setUpViewModel() {
        binding!!.viewModel=settingViewModel
    }

    private fun callUpdatePasswordAfterForgotPass() {
        settingViewModel!!.updatePasswordAfterForgotRequest(createUpdatePasswordAfterForgotRequestBody())
        settingViewModel!!.setIsLoading(true,requireActivity())

        settingViewModel!!.changePassResponse.observe(viewLifecycleOwner, Observer { data->
            settingViewModel!!.setIsLoading(false,requireActivity())

            when (data!!.status) {
                Status.SUCCESS->{
                    if(data!!.data!!.code==1) {
                        utilities!!.showAlert("Success", data!!.data!!.data.success)
                        startActivity(Intent(activity, BootStrapProcessActivity::class.java).putExtra("logout","logout"))
                        requireActivity().finish()
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else
                       utilities!!.showAlert("Error",data!!.data!!.data.error)
                }
                Status.ERROR->{
                    utilities!!.showAlert("Error",utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun createUpdatePasswordAfterForgotRequestBody(): UpdatePasswordAfterForgotPassRequestBody {
        var requestBody = UpdatePasswordAfterForgotPassRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token =  utilities!!.readPref("Auth_Token")
        requestBody.new_password =  binding!!.edNewPass.text.toString()
        return requestBody
    }

    private fun callUpdatePasswordForLoginUserAPI() {
        settingViewModel!!.updatePasswordLoginUser(updatePasswordForLoginUserRequestBody())
        settingViewModel!!.setIsLoading(true,requireActivity())

        settingViewModel!!.changePassResponse.observe(viewLifecycleOwner, Observer { data->
            settingViewModel!!.setIsLoading(false,requireActivity())

            when (data!!.status) {
                Status.SUCCESS->{
                    if(data!!.data!!.code==1) {
                        utilities!!.showAlertWithLayoutId("Success", R.layout.pop_validation_alert,  data!!.data!!.data.success, "","","OK",object :
                            Utilities.AlertViewActions {
                            override fun okAction() {  }
                            override fun cancelAction() { }
                            override fun neturalAction() {
                                checkRememberMe()
                                (requireActivity() as MainActivity).onBackPressed()
                            }
                        } )
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else if(data.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else
                        utilities!!.showAlert("Error",data!!.data!!.data.error)
                }
                Status.ERROR->{
                    utilities!!.showAlert("Error",utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun updatePasswordForLoginUserRequestBody(): UpdatePasswordLoginUserRequestBody {
        var requestBody = UpdatePasswordLoginUserRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token =  utilities!!.readPref("Auth_Token")
        requestBody.current_password =  binding!!.edOldPass.text.toString()
        requestBody.new_password =  binding!!.edNewPass.text.toString()
        requestBody.confirm_password =  binding!!.edConfirmPass.text.toString()
        return requestBody
    }

    fun checkRememberMe()
    {
        if (utilities!!.getRememberPref("rememberMe").trim().isNotEmpty()) {
            if (utilities!!.getRememberPref("rememberMe").equals("true", true)) {

                utilities?.saveRememberMe(
                    utilities!!.getRememberPref("email"),
                    binding!!.edNewPass.text.toString(),
                    true
                )
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if(!requireArguments().getString("changePassRequestType").equals("NotloginUser")) {
            (activity as MainActivity).top_layout!!.visibility = View.VISIBLE
            (activity as MainActivity).bottomNavigationView!!.visibility = View.VISIBLE
        }
    }
}