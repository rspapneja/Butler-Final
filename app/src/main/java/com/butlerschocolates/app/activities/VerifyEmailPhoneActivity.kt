package com.butlerschocolates.app.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.butlerschocolates.app.R
import com.butlerschocolates.app.databinding.ActivityEmailPhoneBinding
import com.butlerschocolates.app.databinding.ActivityVerfiyOtpBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation

class VerifyEmailPhoneActivity : AppCompatActivity() {

    var binding: ActivityEmailPhoneBinding?=null
    var validation: Validation? = null
    var utilities: Utilities? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_email_phone)

        utilities = Utilities(this)
        validation = Validation(this, utilities!!)

        binding!!.edEmail.setText(intent.getStringExtra("email"));

        binding!!.btSubmit.setOnClickListener {

            var b=validation!!.validateVerifyEmailPhone(
                binding!!.edEmail.text.toString(),
                binding!!.countryCodePicker.selectedCountryCode,
                binding!!.edPhone.text.toString()
            )

            if(b)
            {   var intent= Intent()
                intent.putExtra("email",binding!!.edEmail.text.toString());
                intent.putExtra("countryCodePicker",binding!!.countryCodePicker.selectedCountryCode);
                intent.putExtra("phoneno",binding!!.edPhone.text.toString());
                setResult(GlobalConstants.Request_Code_Email_PHONE_Verification,intent);
                finish()
            }
        }
    }
}