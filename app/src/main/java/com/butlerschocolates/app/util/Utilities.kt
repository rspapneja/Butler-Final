package com.butlerschocolates.app.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.butlerschocolates.app.R
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Customer
import com.butlerschocolates.app.model.home.Data
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class Utilities {
    internal var context: Context
    internal var sh: SharedPreferences
    internal lateinit var editor: SharedPreferences.Editor

    constructor(context: Context) {
        this.context = context
        sh = context.getSharedPreferences("ButlerAppSharedPref", Context.MODE_PRIVATE)
    }

    //=================================================
    /* Shared Preference Method read ,write clears*/
    //=================================================
    fun writePref(key: String, value: String) {
        editor = sh.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun readPref(key: String): String {
        return sh.getString(key, "")!!
    }

    //--- Clear All Preferences ---
    fun clearAllPref() {
        sh.edit().clear().commit()
    }

    fun clearPrefKey(prefKey: String) {
        sh.edit().remove(prefKey).commit()
    }

    fun isPrefKeyExist(prefKey: String): Boolean {
        return sh.contains(prefKey)
    }

     /*
     * @method
     * -save remember me status
     * */
    fun saveRememberMe(  email: String,pwd: String, rememberMe: Boolean) {
        var rememberPrefMap = HashMap<String, String>()
        rememberPrefMap.put("password", pwd)
        rememberPrefMap.put("email", email)
        rememberPrefMap.put("rememberMe", if (rememberMe) "true" else "false")
        putRememberPref(rememberPrefMap)
    }

    fun putRememberPref(map: Map<String, String>) {
        val preferences = context.getSharedPreferences(GlobalConstants.REMEMBER_PREF, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val pair = iterator.next() as java.util.Map.Entry<*, *>
            //System.out.println(pair.getKey()+" : "+pair.getValue());
            editor.putString(pair.key.toString().trim({ it <= ' ' }), pair.value.toString().trim({ it <= ' ' }))
        }
        editor.commit()
    }

    //--- Read Preferences -----
    fun getRememberPref(prefKey: String): String {
        val preferences = context.getSharedPreferences(GlobalConstants.REMEMBER_PREF, Context.MODE_PRIVATE)
        return preferences.getString(prefKey.trim { it <= ' ' }, "")!!
    }

    //--- Clear All Preferences ---
    fun clearRememberAllPref() {
        val preferences = context.getSharedPreferences(GlobalConstants.REMEMBER_PREF, Context.MODE_PRIVATE)
        preferences.edit().clear().commit()
    }

    fun isLogin(b: Boolean) {
        writePref("isLogin", b.toString())
    }

    fun saveCustomerDetail(customer: Customer) {
      writePref ("customer", Gson().toJson(customer).toString())
    }

    fun getCustomerDetail():Customer
    {
        val gson: Gson = Gson()
        return gson.fromJson(readPref("customer"), Customer::class.java)
    }

    fun saveHomeApiData(data: Data) {
        writePref ("data", Gson().toJson(data).toString())
    }

    fun getHomeApiData():Data
    {
        val gson: Gson = Gson()
        return gson.fromJson(readPref("data"), Data::class.java)
    }

    /* load image
    * */
    fun loadImage(imageURL:String?, imageView :ImageView) {
      if(imageURL != null) {
       Glide.with(imageView.getContext())
              .load(GlideHeaders.getUrlWithHeaders(AppConstants.BASE_IMAGE_URL + imageURL!!))
              .into(imageView)
      }
    }

    fun loadImageWithFilePath(filepath:String, imageView :ImageView) {
        Glide.with(imageView.getContext())
            .load( filepath)
            .into(imageView)
    }

    fun showTwoDecimalPos(doubleValue: Double?): String {
        return String.format("%.2f", doubleValue)
    }

    fun hideKeyboard(view: View,activity: Activity) {
        val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun apiAlert(throwable: Throwable): String {
        var errorString: String? = null
        if (throwable is UnknownHostException) {
            errorString = context.getString(R.string.no_internetconnection)
        } else if (throwable is SocketTimeoutException) {
            errorString = context.getString(R.string.error_time_out)
        }
        else if(throwable is JsonSyntaxException)
        {
            errorString = context.getString(R.string.error_message)
        }
        else {
            errorString = throwable.localizedMessage.toString()
        }
        return errorString
    }

    //---- Alert Dialog for intent
    fun showAlertWithIntent(title: String, message: String, intent: Intent) {
        val alertDialog: AlertDialog.Builder =
            AlertDialog.Builder(context/*, R.style.AlertDialogStyle*/)
        alertDialog.setTitle(title)

        alertDialog.setMessage(message)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("OK") { dialog, which ->
            context.startActivity(intent)
            dialog.dismiss()
        }
        alertDialog.show()
    }

    //---- Alert Dialog for
    fun showAlert(title: String, message: String) {

        val alertDialog=Dialog(context)
        val layout = LayoutInflater.from(context).inflate( R.layout.pop_validation_alert, null)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        alertDialog!!.setContentView(layout)
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
        val window: Window = alertDialog!!.window!!
        val width = (context.getResources().getDisplayMetrics().widthPixels * 0.90).toInt()

        window.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        var titleText=alertDialog.findViewById<TextView>(R.id.tv_alertTitle)
        var tv_messageText=alertDialog.findViewById<TextView>(R.id.tv_messageText)

        var bt_cancel=alertDialog.findViewById<TextView>(R.id.bt_cancel)
        var bt_ok=alertDialog.findViewById<TextView>(R.id.bt_ok)
        var bt_submit=alertDialog.findViewById<TextView>(R.id.bt_submit)

        titleText!!.setText(title)
        tv_messageText!!.setText(message)
        bt_submit!!.visibility=View.GONE
        bt_cancel!!.visibility=View.GONE
        bt_ok!!.setText("ok")

        bt_ok.setOnClickListener {
            alertDialog.dismiss();
        }
    }

    //---- Alert Dialog with Action Callbacks
    fun showAlertWithActions(
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String?,
        actionButtons: ActionButtons
    ) {
        val alertDialog: AlertDialog.Builder
        alertDialog = AlertDialog.Builder(context/*, R.style.mytheme*/)
        val layout = LayoutInflater.from(context).inflate(R.layout.pop_validation_alert, null)
        alertDialog.setCancelable(false)
        alertDialog!!.setView(layout)

        var dialog = alertDialog.create();
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        var titleText=dialog.findViewById<TextView>(R.id.tv_alertTitle)
        var tv_messageText=dialog.findViewById<TextView>(R.id.tv_messageText)

        var bt_cancel=dialog.findViewById<TextView>(R.id.bt_cancel)
        var bt_ok=dialog.findViewById<TextView>(R.id.bt_ok)
        var bt_submit=dialog.findViewById<TextView>(R.id.bt_submit)
        bt_ok!!.visibility=View.GONE

        titleText!!.setText(title)
        tv_messageText!!.setText(message)

        bt_submit!!.setText(positiveButton)
        bt_cancel!!.setText(negativeButton)

       bt_submit.setOnClickListener {
                dialog!!.dismiss()
                actionButtons.okAction()
            }
            bt_cancel.setOnClickListener {
                dialog.dismiss();
                actionButtons.cancelAction()
            }
    }



    fun showAlertLoyaltyProgram(
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String?,
        platinum_card_terms: String?,
        actionButtons: ActionButtons
    ) {
        val alertDialog: AlertDialog.Builder
        alertDialog = AlertDialog.Builder(context/*, R.style.mytheme*/)
        val layout = LayoutInflater.from(context).inflate(R.layout.pop_validation_alert, null)
        alertDialog.setCancelable(false)
        alertDialog!!.setView(layout)

        var dialog = alertDialog.create();
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        var titleText=dialog.findViewById<TextView>(R.id.tv_alertTitle)
        var tv_messageText=dialog.findViewById<TextView>(R.id.tv_messageText)
        var layout_terms=dialog.findViewById<LinearLayout>(R.id.layout_terms)
        var chkbox_Terms=dialog.findViewById<CheckBox>(R.id.chkbox_Terms)
        var txt_Terms=dialog.findViewById<TextView>(R.id.txt_Terms)

        makeClickableSpan(platinum_card_terms,txt_Terms)

        var bt_cancel=dialog.findViewById<TextView>(R.id.bt_cancel)
        var bt_ok=dialog.findViewById<TextView>(R.id.bt_ok)
        var bt_submit=dialog.findViewById<TextView>(R.id.bt_submit)
        bt_ok!!.visibility=View.GONE
        layout_terms!!.visibility=View.VISIBLE

        titleText!!.setText(title)
        tv_messageText!!.setText(message)

        bt_submit!!.setText(positiveButton)
        bt_cancel!!.setText(negativeButton)

        bt_submit.setOnClickListener {
          if(chkbox_Terms!!.isChecked) {
              dialog!!.dismiss()
              actionButtons.okAction()
          }
            else
          {
              Toast.makeText(context,"Please Check terms and Condtions",Toast.LENGTH_SHORT).show()
          }
        }
        bt_cancel.setOnClickListener {
            dialog.dismiss();
            actionButtons.cancelAction()
        }
    }

    //---- Alert Dialog with Action Callbacks
    fun showAlertWithLayoutId(
        title: String?,
        layout: Int,
        message: String,
        positiveButton: String?,
        negativeButton: String?,
        neturalButton: String?,
        alertActions: AlertViewActions
    ) {
        var builderDialog:AlertDialog.Builder?=null
        val layout = LayoutInflater.from(context).inflate(layout, null)
        builderDialog = AlertDialog.Builder(context/*, R.style.mytheme*/)
        builderDialog!!.setCancelable(false)
        builderDialog!!.setView(layout)
        var dialog = builderDialog.create();
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

       // val dialog_address: AlertDialog = builderDialog!!.show()

        var titleText=dialog.findViewById<TextView>(R.id.tv_alertTitle)
        var tv_messageText=dialog.findViewById<TextView>(R.id.tv_messageText)

        var bt_cancel=dialog.findViewById<TextView>(R.id.bt_cancel)
        var bt_ok=dialog.findViewById<TextView>(R.id.bt_ok)
        var bt_submit=dialog.findViewById<TextView>(R.id.bt_submit)

        titleText!!.setText(title)
        tv_messageText!!.setText(message)
        bt_submit!!.setText(positiveButton)
        bt_cancel!!.setText(negativeButton)
        bt_ok!!.setText(neturalButton)

        if (!positiveButton.equals("") ) {
             bt_submit.setOnClickListener {
                dialog!!.dismiss()
                alertActions.okAction()
            }
        }   else{ bt_submit.visibility=View.GONE }

        if (!negativeButton.equals("")) {
            bt_cancel.setOnClickListener {
                dialog.dismiss();
                alertActions.cancelAction()
            }
        }
        else{ bt_cancel.visibility=View.GONE}

        if (!neturalButton.equals("")) {
           bt_ok.setOnClickListener {
                dialog.dismiss();
               alertActions.neturalAction()
            }
        }
        else{ bt_ok.visibility=View.GONE }
    }

    interface AlertViewActions {
        fun okAction()
        fun cancelAction()
        fun neturalAction()
    }

    //---- Alert actions
    interface ActionButtons {
        fun okAction()
        fun cancelAction()
    }

    /*
   * @method
   * -open url
   * */
    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
             Toast.makeText(context,"No application found to handle!!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeClickableSpan(
        platinumCardTerms: String?,
        txtTerms: TextView?
    ) {
        val ss = SpannableString("TERMS AND CONDITIONS")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override  fun onClick(widget: View) {
                openUrl(platinumCardTerms!!)
            }
            override  fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(true)
            }
        }
        ss.setSpan(clickableSpan, 0 , ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtTerms!!.text = ss
        txtTerms!!.movementMethod = LinkMovementMethod.getInstance()
        txtTerms!!.highlightColor = Color.TRANSPARENT
    }

    fun getAppVerion(): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = "v${pInfo.versionName}"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }
}