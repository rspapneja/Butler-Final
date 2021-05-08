package com.butlerschocolates.app.fragment.signup


import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView.BufferType
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.BootStrapProcessActivity
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.activities.VerifyOtpActivity
import com.butlerschocolates.app.application.ButlersApplication
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.callback.FragmentToActivityCallback
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.signup.SignupRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Utilities.AlertViewActions
import com.butlerschocolates.app.util.Validation
import com.butlerschocolates.app.viewmodels.signup.SignupViewModel
import com.hbb20.CountryCodePicker


class SignupFragment : BaseFragment(), View.OnClickListener,CountryCodePicker.OnCountryChangeListener {

    // EditText
    var ed_name: EditText? = null
    var ed_email: EditText? = null
    var ed_mobile: EditText? = null
    var ed_password: EditText? = null
    var ed_confirmPass: EditText? = null

    // TextView
    var txt_Terms: TextView? = null
    var countryCodeText:TextView?=null

    // CheckBox
    var chkbox_Terms: CheckBox? = null
    var chkBox_RememberMe: CheckBox? = null

    // Button
    var bt_facebook: Button? = null
    var bt_submit: Button? = null


    var validation: Validation? = null

    // View Model
    var viewModel: SignupViewModel? = null

    var fragmentToActivityCallback: FragmentToActivityCallback? = null

    var countryCodePicker:CountryCodePicker?=null


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            fragmentToActivityCallback = activity as FragmentToActivityCallback
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_sign_up, container, false)

        viewInitailze(v)
        setupListerner()

        setupViewModel()
        hideAndShowProgressBar()

        return v
    }

    private fun callSignUpApi() {
        if (ButlersApplication.instance!!.isConnected) {
            viewModel!!.doSignUpRequest(requireContext(),createSignupRequestBody())!!.observe(
                requireActivity(),
                Observer {
                   // call is successful
                    if (it!!.getData() != null) {
                         utilities!!.showAlertWithLayoutId("Success", R.layout.pop_validation_alert,  it.getData().data.success, "","","OK",object : AlertViewActions{
                            override fun okAction(){}
                            override fun cancelAction(){}
                            override fun neturalAction(){
                                openVerfiyOTPScreen(it.getData().data.customerId.toString(),ed_email?.text.toString())
                            }
                        } )
                    } else {
                        // call failed.
                        var e = it.error
                        utilities!!.showAlert("Error", utilities!!.apiAlert(e).toString())
                    }
                }
            )
        } else {
            utilities!!.showAlert("Error", getString(R.string.no_internetconnection))
        }
    }

    private fun openVerfiyOTPScreen(customerId: String, email: String) {
        val intent = Intent(activity, VerifyOtpActivity::class.java)
        intent.putExtra("cid", customerId)
        intent.putExtra("from", "register")
        intent.putExtra("email", email)
        startActivityForResult(intent, GlobalConstants.Request_Code_SMS_Verification)
    }

    private fun openMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun hideAndShowProgressBar() {
        viewModel!!.progressbarObservable!!.observe(requireActivity(),
            Observer<Boolean> { progressObserve ->
                if (progressObserve!!) {
                    (activity as BootStrapProcessActivity).progressbar!!.show()
                } else {
                    (activity as BootStrapProcessActivity).progressbar!!.hide()
                }
            })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)
        viewModel!!.init()
    }

    private fun setupListerner() {
        bt_facebook!!.setOnClickListener(this)
        bt_submit!!.setOnClickListener(this)
        countryCodeText!!.setOnClickListener(this)
        countryCodePicker!!.setOnCountryChangeListener(this)
    }

    private fun viewInitailze(v: View?) {

        utilities = Utilities(requireActivity())
        validation = Validation(requireActivity(), utilities!!)

        // editText
        ed_name = v?.findViewById(R.id.ed_name)
        ed_email = v?.findViewById(R.id.ed_email)
        ed_mobile = v?.findViewById(R.id.ed_moblie)
        ed_password = v?.findViewById(R.id.ed_password)
        ed_confirmPass = v?.findViewById(R.id.ed_confirmPass)

        // TextView
        txt_Terms = v?.findViewById(R.id.txt_Terms)
        countryCodeText = v?.findViewById(R.id.countryCodeText)

        // Check Box
        chkbox_Terms = v?.findViewById(R.id.chkbox_Terms)
        chkBox_RememberMe = v?.findViewById(R.id.chkBox_RememberMe)

        // Button
        bt_facebook = v?.findViewById(R.id.bt_facebook)
        bt_submit = v?.findViewById(R.id.bt_submit)

        // country picker
        countryCodePicker=v?.findViewById(R.id.countryCode_Picker)

       makeClickableSpanTermsAndConditionsPolicy()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_facebook -> {
                fragmentToActivityCallback!!.loginCallback("facebook","","",false);
            }
            R.id.countryCodeText-> {
                countryCodePicker!!.launchCountrySelectionDialog()
            }
            R.id.bt_submit -> {
                var b = validation!!.validateSignup(
                    ed_name?.text.toString(),
                    ed_email?.text.toString(),
                    countryCodePicker!!.selectedCountryCode,
                    ed_mobile?.text.toString(),
                    ed_password?.text.toString(),
                    ed_confirmPass?.text.toString(),
                    chkbox_Terms?.isChecked
                )
                if (b) {
                    utilities!!.showAlertWithActions(
                        "Confirmation",
                        utilities!!.getHomeApiData().register_email_confirmation.replace("(email)",ed_email!!.text.toString()),
                        "CONTINUE",
                        "CANCEL",
                        object : Utilities.ActionButtons {
                            override fun okAction() {
                                callSignUpApi()
                            }
                            override fun cancelAction() {}
                        })
                }
            }
        }
    }

    fun createSignupRequestBody(): SignupRequestBody {
        var requestBody = SignupRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.name = ed_name?.text.toString()
        requestBody.email = ed_email?.text.toString()
      //  requestBody.country_code = countryCodeText!!.text.toString().toInt()
        requestBody.country_code = countryCodePicker!!.selectedCountryCode.toInt()
        requestBody.mobile = ed_mobile?.text.toString()
        requestBody.password = ed_password?.text.toString()
        requestBody.is_terms_req = 1
        return requestBody
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED) {
            if ((requestCode == GlobalConstants.Request_Code_SMS_Verification) || (resultCode == Activity.RESULT_OK)) {
                utilities?.saveRememberMe(
                    ed_email!!.text.toString().trim(),
                    ed_password!!.text.toString().trim(),
                    chkBox_RememberMe!!.isChecked
                )
                openMainScreen()
            }
        }
    }

    override fun onCountrySelected() {
        countryCodeText!!.text = "+${countryCodePicker!!.selectedCountryCode}"
    }
    override fun onDetach() {
        fragmentToActivityCallback = null
        super.onDetach()
    }

    private fun makeClickableSpanTermsAndConditionsPolicy() {
        val spanTxt = SpannableStringBuilder(
            "I agree to the "
        )
        spanTxt.append("Terms & Conditions")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                utilities?.openUrl(utilities!!.getHomeApiData().terms_conditions)
            }
        }, spanTxt.length - "Terms & Conditions ".length, spanTxt.length, 0)
        spanTxt.append(" and ")
        spanTxt.setSpan(ForegroundColorSpan(Color.WHITE), 33, spanTxt.length, 0)
        spanTxt.append(" Privacy Policy")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                utilities?.openUrl(utilities!!.getHomeApiData().privacy_policy)
            }
        }, spanTxt.length - " Privacy Policy".length, spanTxt.length, 0)
        txt_Terms!!.movementMethod = LinkMovementMethod.getInstance()
        txt_Terms!!.setText(spanTxt, BufferType.SPANNABLE)
    }
}
