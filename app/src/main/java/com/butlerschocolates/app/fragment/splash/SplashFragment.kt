package com.butlerschocolates.app.fragment.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.BootStrapProcessActivity
import com.butlerschocolates.app.activities.MainActivity

import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.callback.FragmentToActivityCallback
import com.butlerschocolates.app.model.home.HomeRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.home.HomeViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpashFragment : Fragment(), View.OnClickListener {

    // textView
    var tv_login: TextView?=null
    var tv_skip: TextView?=null
    var tv_title: TextView?=null
    var tv_desc: TextView?=null

    // Image view
    var splash_image: ImageView?=null

    //Button
    var bt_getStarted: Button?=null
    var bt_facebook_login: Button?=null

    var getStarrtedLayout: RelativeLayout?=null
    var dymanicSplashLayout: LinearLayout?=null

    //View Model
    var homeViewModel: HomeViewModel? = null


    var utilities: Utilities? = null

    var fragmentToActivityCallback: FragmentToActivityCallback? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            fragmentToActivityCallback = activity as FragmentToActivityCallback
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v= inflater.inflate(R.layout.fragment_splash_screen, container, false)

        viewInitailze(v)
        setupViewModel()
       hideAndShowProgressBar()
        callHomeApi()
       setupListerner()

        return  v;
    }


 private fun viewInitailze(v: View?) {
        utilities = Utilities(requireActivity())

        tv_skip  = v?.findViewById(R.id.tv_skip)
        tv_login  = v?.findViewById(R.id.tv_login)
        tv_title  = v?.findViewById(R.id.tv_title)
        tv_desc  = v?.findViewById(R.id.tv_desc)

        splash_image  = v?.findViewById(R.id.splash_image)

        bt_facebook_login  = v?.findViewById(R.id.bt_facebook_login)
        bt_getStarted  = v?.findViewById(R.id.bt_getStarted)

        getStarrtedLayout  = v?.findViewById(R.id.getStarrtedLayout)
        dymanicSplashLayout  = v?.findViewById(R.id.dymanicSplashLayout)

        if(utilities!!.readPref("isLogin").equals("true"))
        {
            getStarrtedLayout!!.visibility=View.GONE
            dymanicSplashLayout!!.visibility=View.VISIBLE
        }
        else {
            getStarrtedLayout!!.visibility=View.VISIBLE
            dymanicSplashLayout!!.visibility=View.GONE
        }
    }

    private fun setupListerner() {
        tv_skip?.setOnClickListener(this)
        tv_login?.setOnClickListener(this)

        bt_facebook_login?.setOnClickListener(this)
        bt_getStarted?.setOnClickListener(this)
    }

      override fun onClick(p0: View?) {
          when (p0?.id) {
              R.id.bt_getStarted -> {
                  Navigation.findNavController(p0!!).navigate(R.id.signupFragment, null)
              }
              R.id.bt_facebook_login -> {
                  fragmentToActivityCallback!!.loginCallback("facebook","","",false);
              }
              R.id.tv_login -> {
                  Navigation.findNavController(p0!!).navigate(R.id.loginFragment, null)
              }
              R.id.tv_skip -> {
                  val intent=Intent(activity,MainActivity::class.java)
                  startActivity(intent)
                  requireActivity().finish()
              }
          }
      }

    private fun setupViewModel() {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel!!.init()
    }

    fun creatHomeRequestBody(): HomeRequestBody
    {
        var homeRequestBody = HomeRequestBody()
        homeRequestBody.version = AppConstants.API_VERSION
        homeRequestBody.auth_token = utilities!!.readPref("Auth_Token")

        return homeRequestBody
    }

    private fun callHomeApi() {

            homeViewModel!!.onHomeRequest(requireActivity(),creatHomeRequestBody())!!.observe(requireActivity(), Observer {

                if (it!!.getData() != null)
                {
                    utilities!!.saveHomeApiData(it!!.getData().data)

                    if(utilities!!.readPref("isLogin").equals("true"))
                    {
                        tv_title!!.text=it!!.getData().data.splash_title
                        tv_desc!!.text=it!!.getData().data.splash_desc
                        utilities!!.loadImage(it!!.getData().data.splash_image,splash_image!!)

                        if(it!!.getData().data.customer!=null)
                        utilities!!.saveCustomerDetail(it!!.getData().data.customer)

                          GlobalScope.launch (Dispatchers.Main) {
                            delay(1000L)
                            startActivity(Intent(context, MainActivity::class.java))
                            activity?.finish()
                      }
                    }
                }
                else {
                    // call failed.
                    var e = it.error
                    utilities!!.showAlert("Error",utilities!!.apiAlert(e).toString())
                }
            })
    }

    private fun hideAndShowProgressBar() {
        homeViewModel!!.progressbarObservable!!.observe(requireActivity(),
            Observer<Boolean> { progressObserve ->
                if (progressObserve!!) {
                    (activity as BootStrapProcessActivity).progressbar!!.show()
                } else {
                    (activity as BootStrapProcessActivity).progressbar!!.hide()
                }
            })
    }

    override fun onDetach() {
        fragmentToActivityCallback = null
        super.onDetach()
    }
}
