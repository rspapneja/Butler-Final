package com.butlerschocolates.app.fragment.webview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.util.Utilities
import com.google.android.material.bottomnavigation.BottomNavigationView


class WebViewFragment : BaseFragment() {

    val TAG = "TagWebViewFragment"

    var webView: WebView? = null

    var bundle: Bundle? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        initView(view)
        return view
    }

     fun initView(view: View) {

        utilities = Utilities(activity as MainActivity)

        bindView(view)
    }

    private fun bindView(view: View) {
        webView = view.findViewById(R.id.webView)

        progressbar!!.show()

        webView!!.setWebViewClient(MyWebViewClient())
        webView!!.settings.javaScriptEnabled = true
        webView!!.loadUrl(GlobalConstants.URL)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).findViewById<TextView>(R.id.txt_title).setText(GlobalConstants.ScreenTitle)
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility=View.GONE
    }

    inner class MyWebViewClient : WebViewClient()
    {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
           progressbar!!.hide()
        }
    }

    override fun onStop() {
        super.onStop()
         progressbar!!.hide()
        (activity as MainActivity).findViewById<LinearLayout>(R.id.balance_layout).visibility=View.VISIBLE
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility=View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        progressbar!!.hide()
    }
}
