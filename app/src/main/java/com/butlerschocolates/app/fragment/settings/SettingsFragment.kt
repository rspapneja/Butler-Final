package com.butlerschocolates.app.fragment.settings

import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentSettingsBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.setting.SettingRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.viewmodels.setting.SettingViewModel


class SettingsFragment : BaseFragment() {

    var binding: FragmentSettingsBinding? = null
    val settingViewModel:SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        val view: View = binding!!.getRoot()

        binding!!.viewModel=settingViewModel
        binding!!.fragment=this

        callAppSettingApi("get")
        changePasswordFieldHideOrShow()

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<TextView>(R.id.txt_title).setText(GlobalConstants.ScreenTitle)
    }

    private fun callAppSettingApi(type:String) {
        settingViewModel!!.getAndUpdateAppSettingRequest(createAppSettingRequestBody(type),requireActivity())
        settingViewModel!!.setIsLoading(true,requireActivity())

        settingViewModel!!.settingResponse.observe(viewLifecycleOwner, Observer { data->
            when (data!!.status) {
                Status.SUCCESS->{

                    settingViewModel!!.setIsLoading(false,requireActivity())

                    if(data!!.data!!.code==1)
                    {
                        if(type.equals("get"))
                        {
                          binding!!.switchNotiEmail.isChecked= if(data!!.data!!.data.setting.notiEmailStatus.toInt()==1) true else false
                          binding!!.switchNoti.isChecked=if(data!!.data!!.data.setting.notiNotificationStatus.toInt()==1) true else false
                          binding!!.switchNotiSms.isChecked=if(data!!.data!!.data.setting.notiSmsStatus.toInt()==1) true else false

                          binding!!.switchAccountEmail.isChecked=if(data!!.data!!.data.setting.accEmailStatus.toInt()==1) true else false
                          binding!!.switchAccountMessage.isChecked=if(data!!.data!!.data.setting.accMessageStatus.toInt()==1) true else false

                          binding!!.switchPrivacyMessage.isChecked=if(data!!.data!!.data.setting.plcMessageStatus.toInt()==1) true else false
                          binding!!.switchPrivacyEmail.isChecked=if(data!!.data!!.data.setting.plcEmailStatus.toInt()==1) true else false
                          binding!!.switchPrivacyPhoneCall.isChecked=if(data!!.data!!.data.setting.plcPhoneStatus.toInt()==1) true else false
                         }
                        else{
                            utilities!!.showAlert("Success",data!!.data!!.data.success)
                        }
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
                    settingViewModel!!.setIsLoading(false,requireActivity())
                    utilities!!.showAlert("Error",utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun createAppSettingRequestBody(type:String): SettingRequestBody {
        var requestBody = SettingRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token =  utilities!!.readPref("Auth_Token")
        requestBody.type =  type

        if(type.equals("put")) {
            requestBody.noti_email_status = if (binding!!.switchNotiEmail.isChecked) 1 else 0
            requestBody.noti_sms_status = if (binding!!.switchNotiSms.isChecked) 1 else 0
            requestBody.noti_notification_status = if (binding!!.switchNoti.isChecked) 1 else 0
            requestBody.acc_email_status = if (binding!!.switchAccountEmail.isChecked) 1 else 0
            requestBody.acc_message_status = if (binding!!.switchAccountMessage.isChecked) 1 else 0
            requestBody.plc_email_status = if (binding!!.switchPrivacyEmail.isChecked) 1 else 0
            requestBody.plc_message_status = if (binding!!.switchPrivacyMessage.isChecked) 1 else 0
            requestBody.plc_phone_status = if (binding!!.switchPrivacyPhoneCall.isChecked) 1 else 0
        }
        return requestBody
    }

    fun onSwitchValueChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView.isPressed()) {
            callAppSettingApi("put")
        }
    }

    fun openChangePasswordScreen() {
        val bundle = Bundle()
        bundle.putString("changePassRequestType", "LoginUser")
        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.changePassword,bundle)
    }

    fun changePasswordFieldHideOrShow() {
        var customer = utilities!!.getCustomerDetail()

        if(customer!!.loginfrom.equals("app",true))
        {
            binding!!.buttonChangePassword.visibility=View.VISIBLE
        }
    }
}
