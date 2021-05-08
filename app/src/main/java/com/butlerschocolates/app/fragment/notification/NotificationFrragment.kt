package com.butlerschocolates.app.fragment.notification

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentGeneralNotificationFrragmentBinding
import com.butlerschocolates.app.model.notification.NotificationRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.viewmodels.notification.NotificationViewModel


class NotificationFrragment : BaseFragment() {

    var binding: FragmentGeneralNotificationFrragmentBinding? = null
    val viewModel: NotificationViewModel by viewModels()

    var notification_id=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // Inflate the layout for this fragment
      this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_general_notification_frragment, container, false)
        val view: View = binding!!.getRoot()

        getAreguments()

        callNotificationDetail()

        binding!!.closeButton.setOnClickListener{
            if(this.requireArguments().getString("ityp").equals("general")) {
                (activity as MainActivity).onBackPressed()
            }
            else if(this.requireArguments().getString("ityp").equals("app_store")) {
                val appPackageName: String =
                    requireActivity().packageName // getPackageName() from Context or Activity object

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            }
        }

        return view
    }

    private fun getAreguments() {
        if (this.arguments != null) {
            notification_id=this.requireArguments().getString("id")!!.toInt()
            if (this.requireArguments().getString("ityp").equals("general")) {
                (activity as MainActivity).findViewById<TextView>(R.id.txt_title)
                    .setText(
                        this.requireArguments().getString("title")
                    )

                binding!!.closeButton.setText("Close")
                binding!!.closeButton.visibility = View.VISIBLE
            } else {
                binding!!.closeButton.setText("Update")
                binding!!.closeButton.visibility = View.VISIBLE

                (activity as MainActivity).findViewById<TextView>(R.id.txt_title)
                    .setText("App Update")
            }
        }
    }

    private fun callNotificationDetail() {
        viewModel!!.notificationRequest(confirmNotificationRequestBody())
        viewModel?.setIsLoading(true, requireActivity())

        viewModel!!.notificationResponse.observe(requireActivity(), Observer { data ->
            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel!!.setIsLoading(false, requireActivity())
                    if (data.data!!.code == 1) {

                        if(data.data!!.data.ityp.equals("general")) {
                            (activity as MainActivity).findViewById<TextView>(R.id.txt_title)
                                .setText(
                                    this.requireArguments().getString("title")
                                )

                            binding!!.closeButton.setText("Close")
                            binding!!.closeButton.visibility=View.VISIBLE
                        }
                        else{
                            binding!!.closeButton.setText("Update")
                            binding!!.closeButton.visibility=View.VISIBLE

                            (activity as MainActivity).findViewById<TextView>(R.id.txt_title)
                                .setText("App Update")
                        }
                     utilities!!.loadImage(data.data!!.data.image,  binding!!.image)
                     binding!!.text.setText(data.data!!.data.message)
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else
                        utilities!!.showAlert("Error", data!!.data!!.data.error)
                }
                Status.ERROR -> {
                    viewModel!!.setIsLoading(false, requireActivity())
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun confirmNotificationRequestBody(): NotificationRequestBody {
        var requestBody = NotificationRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
       if(notification_id!=0)
        requestBody.id =notification_id
        return requestBody
    }
}