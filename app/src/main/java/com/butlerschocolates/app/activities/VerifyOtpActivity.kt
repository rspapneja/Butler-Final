package com.butlerschocolates.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.butlerschocolates.app.R
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseActivity
import com.butlerschocolates.app.databinding.ActivityVerfiyOtpBinding
import com.butlerschocolates.app.model.common.Customer
import com.butlerschocolates.app.model.resendotp.OtpRequestBody
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyRequestBody
import com.butlerschocolates.app.model.verifyotp.VerifyOtpRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation
import com.butlerschocolates.app.viewmodels.verfiyotp.VerifyOtpViewModel
import kotlinx.android.synthetic.main.activity_verfiy_otp.*
import java.util.concurrent.TimeUnit


class VerifyOtpActivity : BaseActivity() {

    // views
    var ed_verifyotp: EditText? = null
    var bt_verify: Button? = null

    // view model
    var viewModel: VerifyOtpViewModel? = null

    var utilities: Utilities? = null
    var validation: Validation? = null

    var cid: String? = null

    val FORMAT = "%02d:%02d"
    var countDownDuration = 120000L
    var timerTxt: TextView? = null

    var emailTxt: TextView? = null
    var editEmail: ImageButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityVerfiyOtpBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_verfiy_otp
        )

        viewInitailze()
        setupViewModel(binding)
        countDownTimer.start()

        //--- hideAndShowProgressBar()

        bt_verify!!.setOnClickListener {
            if (bt_verify?.text.toString().equals("verify", true)) {
                var b = validation!!.validateVerifyOtp(ed_verifyotp?.text.toString())
                if (b) {
                    callVerifyOtp()
                }
            } else if (bt_verify?.text.toString().equals("re-send", true)) {
                requestOtp()
            }
        }

        editEmail?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun viewInitailze() {
        utilities = Utilities(this@VerifyOtpActivity)
        validation = Validation(this@VerifyOtpActivity, utilities!!)

        // editText
        ed_verifyotp = findViewById(R.id.ed_verifyotp)
        bt_verify = findViewById(R.id.bt_verify)
        timerTxt = findViewById(R.id.timerTxt)

        emailTxt = findViewById(R.id.email_Txt)
        editEmail = findViewById(R.id.btn_editEmail)

        if (intent.hasExtra("email")) {
            emailTxt?.text = intent.getStringExtra("email")
        }
    }

    private fun setupViewModel(binding: ActivityVerfiyOtpBinding) {
        viewModel = ViewModelProviders.of(this).get(VerifyOtpViewModel::class.java)
        viewModel!!.init()
        binding.viewModel = viewModel
    }

    fun createVerifyRequestBody(): VerifyOtpRequestBody {
        var requestBody = VerifyOtpRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.otp = ed_verifyotp?.text.toString().toInt()
        requestBody.device_id = newToken
        requestBody.device_type = "android"
        requestBody.cid = intent!!.getStringExtra("cid")
        return requestBody
    }

    private fun callVerifyOtp() {
        viewModel!!.verifyOtpRequest(this@VerifyOtpActivity, createVerifyRequestBody())!!.observe(
            this@VerifyOtpActivity,
            Observer {

                // call is successful
                viewModel?.setIsLoading(false)

                if (it!!.getData() != null) {

                    cid = intent!!.getStringExtra("cid")
                    utilities?.writePref(
                        "Auth_Token",
                        it!!.getData().data!!.customer?.authToken.toString()
                    )

                    if (it!!.getData().code == 1) {
                        intentAction(true, it!!.getData().data.customer!!)
                    } else if (it!!.getData().code == 7) {

                        if (it!!.getData().data.customer.syncWithButlers == 0) {
                            utilities!!.showAlertLoyaltyProgram(
                                "Loyalty", it!!.getData().data.ask_enroll, "YES", "NO",
                                utilities!!.getHomeApiData().platinum_card_terms,
                                object : Utilities.ActionButtons {
                                    override fun okAction() {
                                        syncLoyaltyRequest(it!!.getData().data.customer!!)
                                    }

                                    override fun cancelAction() {
                                        intentAction(true, it!!.getData().data.customer!!)
                                    }
                                })
                        }
                    } else {
                        intentAction(false, it!!.getData().data.customer)
                    }
                } else {
                    // call failed.
                    utilities!!.showAlert("Error", utilities!!.apiAlert(it.error))
                }
            }
        )
    }

    private fun syncLoyaltyRequest(customer: Customer) {
        viewModel!!.syncLoyaltyRequest(createSyncLoyalityRequestBody())
        viewModel?.setIsLoading(true, this)

        viewModel!!.syncLoyaltyResponse.observe(this, Observer { data ->
            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel!!.setIsLoading(false, this)
                    if (data.data!!.code == 1) {
                        showAlert(true, data.data.data.customer!!, "Success", data.data.data.success)
                    } else {
                        showAlert(true, customer, "Error", data.data.data.error!!)
                    }
                }
                Status.ERROR -> {
                    viewModel!!.setIsLoading(false, this)
                    showAlert(true, customer, "Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun createSyncLoyalityRequestBody(): SyncLoyaltyRequestBody {
        var requestBody = SyncLoyaltyRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        return requestBody
    }

    private fun requestOtp() {

        viewModel!!.resendOtpRequest(createOtpRequestBody())
        viewModel?.setIsLoading(true, this)

        viewModel!!.resendOtpResponse.observe(this, Observer { data ->
            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel!!.setIsLoading(false, this)
                    if (data.data!!.code == 1) {
                        utilities!!.showAlert("Success", data!!.data!!.data.success)
                        countDownTimer.start()
                        bt_verify?.text = "VERIFY"

                    } else {
                        utilities!!.showAlert("Error", data!!.data!!.data.error)
                    }
                }
                Status.ERROR -> {
                    viewModel!!.setIsLoading(false, this)
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })

    }


    private fun createOtpRequestBody(): OtpRequestBody {
        var requestBody = OtpRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.customerId = intent!!.getStringExtra("cid")?.toInt()
        requestBody.OtpType = intent!!.getStringExtra("from")
        return requestBody
    }

    //countdown timer
    private val countDownTimer = object : CountDownTimer(countDownDuration, 1000) {
        override fun onTick(millisUntilFinished: Long) {

            var minutes =
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                )
            var seconds =
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                )

            var unit = ""
            if (minutes != 0L) {
                unit = "MIN"
            } else {
                unit = "SEC"
            }

            timerTxt?.text = "${String.format(FORMAT, minutes, seconds)} $unit"
        }

        override fun onFinish() {
            cancel()
            timerTxt?.text = "00:00 SEC"
            bt_verify?.text = "RE-SEND"
        }
    }

    fun intentAction(save: Boolean, customer: Customer) {
        if (save) {
            utilities?.saveCustomerDetail(customer)
            utilities?.isLogin(save)
        }

        val intent = Intent()
        intent.putExtra("cid", cid)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    fun showAlert(save: Boolean, customer: Customer, title: String, message: String) {
        utilities!!.showAlertWithLayoutId(
            title,
            R.layout.pop_validation_alert,
            message,
            "",
            "",
            "OK",
            object :
                Utilities.AlertViewActions {
                override fun okAction() {}
                override fun cancelAction() {}
                override fun neturalAction() {
                    intentAction(save, customer)
                }
            })
    }

}
