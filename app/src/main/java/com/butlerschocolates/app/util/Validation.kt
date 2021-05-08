package com.butlerschocolates.app.util

import android.content.Context
import android.widget.EditText
import com.butlerschocolates.app.cardvalidation.ExpiryDateEditText
import mostafa.ma.saleh.gmail.com.editcredit.EditCredit


class Validation {

    internal var context: Context
    internal var utilities: Utilities

    constructor(context: Context,utilities: Utilities) {
        this.context = context
        this.utilities = utilities
    }

    fun validateSignup(
        name: String,
        email: String,
        countryCode: String,
        number: String,
        pwd: String,
        confirmPwd: String,
        checked: Boolean?
    ):Boolean {
        if (name.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Sign up","Please enter name!!")
            return false
        } else if (email.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Sign up","Please enter email!!")
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            utilities!!.showAlert("Sign up","Please enter valid email!!")
            return false
        } else if (countryCode.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Sign up","Please select country code!!")
            return false
        } else if (number.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Sign up","Please enter mobile number!!")
            return false
        } else if (pwd.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Sign up","Please enter password!!")
            return false
        } else if (confirmPwd.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Sign up","Please enter confirm password!!")
            return false
        }
       /* if (pwd.length < 8 || pwd.length >= 12) {
            val message =
                "New Password length should be 8 to 12 Character"
            utilities!!.showAlert("Alert",message)
            return   false
        }*/
        else if (!pwd.trim { it <= ' ' }
                .equals(confirmPwd.trim { it <= ' ' }, ignoreCase = true)) {
            utilities!!.showAlert("Sign up","Password mismatch!!")
            return false
        } else if (!checked!!) {
            utilities!!.showAlert("Sign up","Please check terms & conditions!!")
            return false
        } else {
            return true
        }
    }

    fun validateVerifyOtp(
        otp: String
    ):Boolean {
        if (otp.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("One Time Password","Please enter One Time Password!!")
            return false
        } else {
            return true
        }
    }

    fun validateVerifyEmailPhone(
        email: String,
        countryCode: String,
        number: String
    ):Boolean {
        if (email.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Login","Please enter email!!")
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            utilities!!.showAlert("Login","Please enter valid email!!")
            return false
        } else if (countryCode.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Login","Please select country code!!")
            return false
        } else if (number.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Login","Please enter mobile number!!")
            return false
        }  else {
            return true
        }
    }

    fun validateForgotPasswordForm(
        email: String
    ):Boolean {
        if (email.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Forgot Password","Please enter email!!")
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            utilities!!.showAlert("Forgot Password","Please enter valid email!!")
            return false
        }  else {
            return true
        }
    }

     fun cardValidate(
        newCardName: EditText,
        newCardNumber: EditCredit,
        date: ExpiryDateEditText,
        cvv: EditText
    ):Boolean {
        if (newCardName!!.text.toString().trim() == "" || newCardNumber!!.text.toString().trim() == "" || date!!.text.toString().trim() == "" || cvv!!.text.toString().trim() == "") {
            utilities!!.showAlert("Card","Enter all Card details")
            return false
        } else if (!newCardNumber!!.isCardValid) {
            utilities!!.showAlert("Card","Invalid Card")
            return false
        } else if (date!!.year == null) {
            utilities!!.showAlert("Card","Invalid Expiry")
            return false
        } else {
            return true
        }
    }

    fun changePasswordForLoginValidate(
        old_password: String,
        new_password: String,
        cnf_password: String
    ): Boolean {
         if (old_password.length == 0 || new_password.length == 0 || cnf_password.length == 0) {
            val message = "Enter all details"
            utilities!!.showAlert("Change Password",message)
             return   false
        } /*else if (new_password.length < 8 || new_password.length >= 12) {
            val message =
                "New Password length should be 8 to 12 Character"
            utilities!!.showAlert("Alert",message)
             return   false
        }*/ else if (new_password == cnf_password) {
             return   true
        } else {
            val message = "New Password and Confirm Password are not match"
            utilities!!.showAlert("Change Password",message)
             return  false
        }
    }

    fun changePasswordAfterForgotValidate(
        new_password: String,
        cnf_password: String
    ): Boolean {
        return if (new_password.length == 0 || cnf_password.length == 0) {
            val message = "Enter all details"
            utilities!!.showAlert("Change Password",message)
            return  false
        }/* else if (new_password.length < 8 || new_password.length >= 12) {
            val message =
                "New Password length should be 8 to 12 Character"
            utilities!!.showAlert("Alert",message)
            return false
        }*/ else if (new_password == cnf_password) {
            true
        } else {
            val message = "New Password and Confirm Password are not match"
            utilities!!.showAlert("Change Password",message)
            false
        }
    }


    fun validateBillingAddress(
        billing_address:String,billing_city:String,billing_county:String,billing_postal_code:String
    ):Boolean {
        if (billing_address.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Address","Please enter Address!!")
            return false
        } else if (billing_county.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Address","Select the country!!")
            return false
        } else if (billing_city.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Address","Please select city!!")
            return false
        } else if (billing_postal_code.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Address","Please enter Postal Code!!")
            return false
        }  else {
            return true
        }
    }

    fun loginValidate(email: String, pwd: String): Boolean {
        if (email.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Login","Please enter email!!")
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            utilities!!.showAlert("Login","Please enter valid email!!")
            return false
        }  else if (pwd.trim { it <= ' ' }.isEmpty()) {
            utilities!!.showAlert("Login","Please enter password!!")
            return false
        }
        else
            return  true
    }
}