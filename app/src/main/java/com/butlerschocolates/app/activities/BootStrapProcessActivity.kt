package com.butlerschocolates.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import com.butlerschocolates.app.R
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseActivity
import com.butlerschocolates.app.callback.FragmentToActivityCallback

import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.global.GlobalConstants.Request_Code_Email_PHONE_Verification
import com.butlerschocolates.app.model.login.LoginReqestBody
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.ProgressBarHandler
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.login.LoginViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


class BootStrapProcessActivity : BaseActivity() , FragmentToActivityCallback {

    var utilities: Utilities? = null
    var EMAIL: String = "email"

    //fb callbackManager
    var callbackManager: CallbackManager? = null

    // view model
    var viewModel: LoginViewModel? = null

    var progressbar: ProgressBarHandler? = null
    var requestBody:LoginReqestBody?=null

    var emailString:String?=null

    var isFacebookRequestData=false
    var email_id=""
    var country_code=""
    var phone=""

    var passwordString=""
    var emailIdString=""
    var checkRememberStaus=false

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bootstrap)

       if(intent!=null && (intent.hasExtra("logout")||(intent.hasExtra("redirectBackToCartScreen"))))
       {
           if(intent.hasExtra("logout"))
           {
             LoginManager.getInstance().logOut();
           }
           openLoginScreen()

       }

        initializeView()
        setupViewModel()
      //  getkeyhash()

    }

   private fun openLoginScreen() {
      findNavController(this@BootStrapProcessActivity,R.id.nav_host_fragment).navigate(R.id.action_spashFragment_to_loginFragment,null,NavOptions.Builder()
          .setPopUpTo(R.id.spashFragment,
              true).build())
  }

    private fun initializeView() {
        utilities = Utilities(this@BootStrapProcessActivity)
        progressbar =  ProgressBarHandler(this@BootStrapProcessActivity);

        // instance of login request boady
        requestBody = LoginReqestBody()

        //fb callbackManager
        callbackManager = CallbackManager.Factory.create()
        //fb callback
        fbLoginManager()
    }

    /*
    * @method
    * -initialize fb manger-callback
    * */
    fun fbLoginManager() {
        LoginManager.getInstance().registerCallback(callbackManager!!,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                   // App code
                    Log.e("Tag loginResult","loginResult"+loginResult)
                    requestGraphApi()
                }
                override fun onCancel() {
                    // App code
                    Log.e("Tag cancel","cancel")
                }
                override fun onError(exception: FacebookException) {
                    // App code
                    Log.e("Tag Error","cancel"+exception)
                    exception.printStackTrace()
                }
            })
    }

    /*
    * @method
    * -launch fb login with permission
    * */
    fun fbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf(EMAIL,"public_profile"))
    }

    /*
    *  @call graph api request from facebook
    * */
    private fun requestGraphApi() {
      val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { `object`, response ->

            // Insert your code here
            requestBody!!.facebook_id =`object`.optString("id")
            requestBody!!.customer_name =`object`.optString("name")
            requestBody!!.email =`object`.optString("email")
            requestBody!!.country_code =""
            requestBody!!.phone =""

            emailString=`object`.optString("email")

          doLoginApiRequest("facebook","","",false)
        }

        val parameters = Bundle()
        parameters.putString("fields","id,name,email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun goToPhoneEmailScreen() {
        val intent = Intent(this@BootStrapProcessActivity, VerifyEmailPhoneActivity::class.java)
        intent.putExtra("email",emailString)
        startActivityForResult(intent,Request_Code_Email_PHONE_Verification);
    }

    private fun openVerfiyOTPScreen(customerId: String, email: String) {
        val intent = Intent(this@BootStrapProcessActivity, VerifyOtpActivity::class.java)
        intent.putExtra("cid", customerId)
        intent.putExtra("islogin", "login")
        intent.putExtra("email", email)
        intent.putExtra("from", "register")
        startActivityForResult(intent, GlobalConstants.REQUEST_CODE_LOGIN_VERIFICATON_CODE)
    }

    //  set up login view model
    private fun  setupViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel!!.init()
    }

    /*
    *  login api
    * */
    private fun doLoginApiRequest(loginType: String?, userEmail: String?, userPassword: String?,chkBox_RememberMe:Boolean)
    {
              emailIdString=userEmail!!
              passwordString=userPassword!!
              checkRememberStaus=chkBox_RememberMe!!

            progressbar!!.show()
            viewModel!!.loginRequest(createLoginRequestBody(loginType,userEmail,userPassword),this@BootStrapProcessActivity)!!.observe(
                this@BootStrapProcessActivity,
                Observer {
                 progressbar!!.hide()

                // call is successful
                   if (it!!.getData() != null)
                    {
                        if(it!!.getData().code==1) {
                            utilities?.isLogin(true)
                            utilities?.saveCustomerDetail(it!!.getData().data.customer!!)
                            utilities?.writePref("Auth_Token",it!!.getData().data!!.customer?.authToken.toString())

                            if (loginType.equals("app")) {
                                utilities?.saveRememberMe(
                                    userEmail!!.trim(),
                                    userPassword!!.trim(),
                                    chkBox_RememberMe
                                )
                            }

                            if(intent!=null && intent.hasExtra("redirectBackToCartScreen")) {
                                setResult(GlobalConstants.REQUEST_CODE_LOGIN, Intent())
                                finish()
                            }
                            else
                                openMainScreen()
                        }
                        else if(it!!.getData().code==3)
                        {
                            goToPhoneEmailScreen()
                        }
                        else if(it!!.getData().code==2)
                        {
                            utilities!!.showAlertWithLayoutId("Login", R.layout.pop_validation_alert,  it.getData().data.success, "","","OK",object :
                                Utilities.AlertViewActions {
                                override fun okAction(){}
                                override fun cancelAction(){}
                                override fun neturalAction(){
                                    openVerfiyOTPScreen(it.getData().data.customer_id.toString(),it.getData().data.displaytxt)
                                }
                            } )
                        }
                        else if(it!!.getData().code==7)
                        {
                            utilities?.isLogin(true)
                            utilities?.saveCustomerDetail(it!!.getData().data.customer!!)
                            utilities?.writePref("Auth_Token",it!!.getData().data!!.customer?.authToken.toString())

                            utilities!!.showAlertWithActions(
                                "Loyalty", utilities?.getHomeApiData()!!.register_loyalty_message,"CONTINUE", "CANCEL",
                                object : Utilities.ActionButtons {
                                    override fun okAction() {
                                        syncLoyaltyRequest()
                                    }
                                    override fun cancelAction() {
                                        utilities?.isLogin(true)
                                        openMainScreen()
                                    }
                                })
                        }
                    } else {
                   // call failed.
                        utilities!!.showAlert("Error",utilities!!.apiAlert(it.error))
                    }
                }
            )
    }

    // create login Request body
    private fun createLoginRequestBody(loginType: String?, userEmail: String?, userPassword: String?): LoginReqestBody
    {
        requestBody!!.version = AppConstants.API_VERSION
        requestBody!!.login_type =loginType

        if(loginType.equals("app")) {
            requestBody!!.email = userEmail
            requestBody!!.password =userPassword
        }

        if(isFacebookRequestData)
        {
            requestBody!!.country_code =country_code
            requestBody!!.phone =phone
            requestBody!!.email=email_id
            isFacebookRequestData=false
        }

        requestBody!!.device_id = deviceToken()
        requestBody!!.device_type = "android"

        return requestBody!!
    }

    /*  open the main activity
    * */
    private fun openMainScreen() {
        val intent= Intent(this@BootStrapProcessActivity, MainActivity::class.java)
        startActivity(intent);
        finish()
    }

    /* @ callback of login facebook native login
    * */
    override fun loginCallback(loginType: String?, userEmail: String?, userPassword: String?,chkBox_RememberMe:Boolean) {

         if(loginType.equals("app"))
             doLoginApiRequest(loginType,userEmail,userPassword,chkBox_RememberMe)
        else
             fbLogin()
    }

    fun fblogin(countrycode: String?, userEmail: String?, phoneNumber: String?) {
        requestBody!!.country_code =countrycode
        requestBody!!.phone =phoneNumber
        requestBody!!.email=userEmail

        doLoginApiRequest("facebook","","",false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != RESULT_CANCELED) {
        if((GlobalConstants.Request_Code_Email_PHONE_Verification==requestCode))
        {
            super.onActivityResult(requestCode, resultCode, data)

            country_code =data!!.getStringExtra("countryCodePicker")!!
            phone =data!!.getStringExtra("phoneno")!!
            email_id=data!!.getStringExtra("email")!!
            isFacebookRequestData=true

           doLoginApiRequest("facebook","","",false)
        }

            if ((requestCode == GlobalConstants.REQUEST_CODE_LOGIN_VERIFICATON_CODE)) {

                doLoginApiRequest("app", emailIdString, passwordString, checkRememberStaus)
            }

        else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        }
        }
    }

    public fun deviceToken():String
    {
        if(newToken!==null  &&  newToken!!.length!=0)
           return newToken!!
        else {
            getToken()
            return Utilities(this@BootStrapProcessActivity).readPref("DeviceToken");
        }

    }

    /* @method call sync loyality when sync loyality status code =1
    * */
    private fun syncLoyaltyRequest()
    {
        viewModel!!.syncLoyaltyRequest(createSyncLoyalityRequestBody())
        progressbar!!.show()

        viewModel!!.syncLoyaltyResponse.observe(this, Observer { data->
            progressbar!!.hide()
            when (data!!.status) {
                Status.SUCCESS->{
                    if(data.data!!.code==1) {
                       utilities?.saveCustomerDetail(data.data.data.customer!!)
                        utilities?.isLogin(true)
                        openMainScreen()
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

    private fun createSyncLoyalityRequestBody(): SyncLoyaltyRequestBody {
        var requestBody = SyncLoyaltyRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token =  utilities!!.readPref("Auth_Token")
        return requestBody
    }
}




