package com.butlerschocolates.app.fragment.forgotpass

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.VerifyOtpActivity
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentForgetPasswordBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.forgotpass.ForgotPasswordRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation
import com.butlerschocolates.app.viewmodels.forgotpass.ForgotPassViewModel


class ForgetPasswordFragment : BaseFragment() {

    var binding: FragmentForgetPasswordBinding? = null
    val viewModel: ForgotPassViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_forget_password, container, false
        )
        val view: View = binding!!.getRoot()

        binding!!.viewModel = viewModel
        var validation=Validation(requireContext(),utilities!!)

        binding!!.btSubmit.setOnClickListener {
           if(validation!!.validateForgotPasswordForm(binding!!.edEmail.text.toString()))
            callForgotPassApi()
        }

       return view
    }

    private fun callForgotPassApi() {

        viewModel!!.forgotPasswordRequest(createForgotPassRequestBody())
        viewModel!!.setIsLoading(true, requireActivity())

        viewModel!!.forgotPassResponse.observe(viewLifecycleOwner, Observer { data ->
            viewModel!!.setIsLoading(false, requireActivity())

            when (data!!.status) {
                Status.SUCCESS -> {
                  if (data!!.data!!.code == 2) {
                        utilities!!.showAlertWithActions(
                                "Confirmation", "One-time password will be sent to your email id. Password will be sent on "+binding!!.edEmail.text.toString()+" Please confirm to proceed", "CONTINUE", "CANCEL",
                        object : Utilities.ActionButtons {
                            override fun okAction() {
                                openVerfiyOTPScreen(data!!.data!!.data.customerId.toString(),binding!!.edEmail.text.toString())
                            }
                            override fun cancelAction() {}
                        })
                    } else
                      utilities!!.showAlert("Error", data!!.data!!.data.error)
                }

                Status.ERROR -> {
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun createForgotPassRequestBody(): ForgotPasswordRequestBody {
        var requestBody = ForgotPasswordRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.email = binding!!.edEmail.text.toString()
        return requestBody
    }

    private fun openVerfiyOTPScreen(customerId: String, email: String) {
        val intent = Intent(activity, VerifyOtpActivity::class.java)
        intent.putExtra("cid", customerId)
        intent.putExtra("from", "forgot")
        intent.putExtra("email", email)
        startActivityForResult(intent, GlobalConstants.Request_Code_SMS_Verification)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED) {
            if ((requestCode == GlobalConstants.Request_Code_SMS_Verification) || (resultCode == Activity.RESULT_OK)) {
                val bundle = Bundle()

                bundle.putString("changePassRequestType", "NotloginUser")
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.changePassword, bundle)
            }
        }
    }
}