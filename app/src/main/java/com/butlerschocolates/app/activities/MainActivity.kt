/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.butlerschocolates.app.activities

import NavigationExtensions
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.butlerschocolates.app.R
import com.butlerschocolates.app.base.BaseActivity
import com.butlerschocolates.app.callback.IOnBackPressed
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rkm.sse.util.ImageUtility


class MainActivity : BaseActivity(), NavController.OnDestinationChangedListener {
    private var currentNavController: LiveData<NavController>? = null

    var progressbar: ProgressBarHandler? = null
    var utilities: Utilities? = null

    override val TAG = "Tag MainActivity"

    var back_FabIcon: ImageView? = null
    var backArrowImag: ImageView? = null
    var balance_layout: LinearLayout? = null
    var top_layout: LinearLayout? = null
    var txt_title: TextView? = null

    var bottomNavigationView: BottomNavigationView? = null
    var fragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeView()

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

        handleNoification()

        back_FabIcon!!.setOnClickListener {
            openCartScreen()
        }
    }

     fun redirectNotificationFragement() {
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        intent.putExtra("title", "Update")
        intent.putExtra("id", "0")
        intent.putExtra("body", "")
        intent.putExtra("ityp", "app_store")
        startActivity(intent)
        finish()
    }

    private fun initializeView() {
        back_FabIcon = findViewById(R.id.back_FabIcon) as ImageView
        backArrowImag = findViewById(R.id.backArrowImag) as ImageView
        balance_layout = findViewById(R.id.balance_layout) as LinearLayout
        top_layout = findViewById(R.id.top_layout) as LinearLayout
        txt_title = findViewById(R.id.txt_title) as TextView

        progressbar = ProgressBarHandler(this@MainActivity);
        utilities = Utilities(this@MainActivity);

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        mCompressor = FileCompressor(this@MainActivity)
        imageUtility = ImageUtility(this@MainActivity)
    }

    private fun handleNoification() {
        var i = intent
        if (i != null) {
            if (i.hasExtra("ityp")) {
                // order Detail
                val bundle = Bundle()
                if (i.getStringExtra("ityp").equals("order")) {

                    GlobalConstants.OrderId = intent.getStringExtra("oid")!!.toInt()
                    bundle.putString("test_oid", GlobalConstants.OrderId.toString())

                    navigateToAllNotifcationFragment(
                        R.navigation.nav_graph_account,
                        R.id.orderDetailFragment,
                        bundle
                    )
                } else if (i.getStringExtra("ityp").equals("support")) {
                    GlobalConstants.NotifyType = 1
                    GlobalConstants.SUPPORT_TICKET_ID = intent.getStringExtra("tid")!!.toInt()

                    navigateToAllNotifcationFragment(
                        R.navigation.nav_graph_account,
                        R.id.supportChatFragment,
                        bundle
                    )
                }
                else if (i.getStringExtra("ityp").equals("store")) {

                    bundle.putString("title", i.getStringExtra("title"))
                    bundle.putString("id", i.getStringExtra("id"))
                    bundle.putString("body", i.getStringExtra("body"))
                    bundle.putString("ityp",i.getStringExtra("ityp"))

                    navigateToAllNotifcationFragment(
                        R.navigation.nav_graph_home,
                        R.id.generalNotificationFrragment,
                        bundle
                    )
                }
                else if (i.getStringExtra("ityp").equals("general")) {

                    bundle.putString("title", i.getStringExtra("title"))
                    bundle.putString("id", i.getStringExtra("id"))
                    bundle.putString("body", i.getStringExtra("body"))
                    bundle.putString("ityp",i.getStringExtra("ityp"))

                    navigateToAllNotifcationFragment(
                        R.navigation.nav_graph_home,
                        R.id.generalNotificationFrragment,
                        bundle
                    )
                }
                else if (i.getStringExtra("ityp").equals("app_store")) {
                    bundle.putString("title", i.getStringExtra("title"))
                    bundle.putString("id", i.getStringExtra("id"))
                    bundle.putString("body", i.getStringExtra("body"))
                    bundle.putString("ityp",i.getStringExtra("ityp"))

                    navigateToAllNotifcationFragment(
                        R.navigation.nav_graph_home,
                        R.id.generalNotificationFrragment,
                        bundle
                    )
                }
            }
        }
    }

    fun navigateToAllNotifcationFragment(
        graphNavId: Int,
        startDestinationGraphId: Int,
        bundle: Bundle
    ) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController!!.navigate(startDestinationGraphId, bundle)
    }

    /*
    *  update the cart value
    * */
    fun updateCartValue() {
        findViewById<TextView>(R.id.totalProductQty).setText(
            databaseHelper!!.getTotalProductsQty().toString()
        )

        if (databaseHelper!!.getTotalProductsQty() == 0)
            findViewById<TextView>(R.id.totalProductQty).visibility = View.GONE
        else
            findViewById<TextView>(R.id.totalProductQty).visibility = View.VISIBLE
    }

    private fun openCartScreen() {
        val intent = Intent(this@MainActivity, CartScreenActivity::class.java)
        startActivityForResult(intent, GlobalConstants.Request_Code_Cart_Screen);
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState!!)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        bottomNavigationView!!.itemIconTintList = null
        fragmentManager = supportFragmentManager

        val navGraphIds = listOf(
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_card,
            R.navigation.nav_graph_account
        )

        // Setup the bottom navigation view with a list of navigation graphs
        var navigationExtensions = NavigationExtensions(bottomNavigationView!!)

        val controller = navigationExtensions.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = fragmentManager!!,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
           navController?.addOnDestinationChangedListener(this)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data!!.data != null) {
            if (requestCode == GlobalConstants.Request_Code_GalleryPicker) {
                this.data = data
                onCaptureImageResult("gallery")
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == GlobalConstants.Request_Code_CameraPicker) {
            onCaptureImageResult("camera")
        } else if (requestCode == GlobalConstants.Request_Code_Cart_Screen) {
            if (data!!.getStringExtra("isPaymentButtonClicked").equals("YES")) {
                openOrderPayment()
            } else {
                //  val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
                val navHostFragment = supportFragmentManager
                    .findFragmentById(R.id.nav_host_container) as NavHostFragment

                if (navHostFragment != null) {
                    val childFragments = navHostFragment.childFragmentManager.fragments
                    childFragments.forEach { fragment ->
                        fragment.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }
    }

    private fun openOrderPayment() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController!!.navigate(R.id.orderPaymentFragment, null)
    }

    /*
   * @callback
   * -destination change listener
   * */
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Console.Log(TAG, "destination tag:${destination.label}")
        //check for fragmeent tag
        checkFragment(destination.label as String, arguments)
    }

    private fun checkFragment(frgLabel: String, arguments: Bundle?) {
        hideShowAccountBalance()
        when (frgLabel) {
            "fragment_customise_your_coffe" -> {
                back_FabIcon!!.visibility = View.VISIBLE
                findViewById<TextView>(R.id.totalProductQty).visibility = View.VISIBLE
            }
            "store_detail_fragment" -> {
                back_FabIcon!!.visibility = View.VISIBLE
                findViewById<TextView>(R.id.totalProductQty).visibility = View.VISIBLE
            }
            "fragment_choose_complimentary_product" -> {
                back_FabIcon!!.visibility = View.VISIBLE
                findViewById<TextView>(R.id.totalProductQty).visibility = View.VISIBLE
            }

            else -> {
                findViewById<TextView>(R.id.totalProductQty).visibility = View.GONE
                back_FabIcon!!.visibility = View.GONE
            }
        }
    }

    /*
    * @Method this is called when auth of api has invalid or expired status code=4
    * */
    fun handleAuthTokenAlert(alertMessage:String) {
        utilities!!.showAlertWithLayoutId(
            "Error",
            R.layout.pop_validation_alert,
            alertMessage,
            "",
            "",
            "OK",
            object :
                Utilities.AlertViewActions {
                override fun okAction() {}
                override fun cancelAction() {}
                override fun neturalAction() {
                    var homeDataString = utilities!!.getHomeApiData()
                    utilities!!.clearAllPref()
                    databaseHelper!!.emptyCart()
                    utilities!!.saveHomeApiData(homeDataString)
                    openLoginScreen()
                }
            })
    }

    private fun openLoginScreen() {
        val intent = Intent(this@MainActivity, BootStrapProcessActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun hideShowAccountBalance() {
        try {
            if (utilities!!.readPref("isLogin").equals("true")) {

                    balance_layout!!.visibility = View.VISIBLE

                    if(utilities!!.showTwoDecimalPos(utilities!!.getCustomerDetail().wallet_amount).equals("0.00"))
                    {
                        txt_title!!.setText(getString(R.string.account_balance))
                    }
                    else
                    {
                        txt_title!!.setText(
                            "PLATINUM LOYALTY BALANCE : " + utilities!!.getCustomerDetail().currencySymbol + utilities!!.showTwoDecimalPos(
                                utilities!!.getCustomerDetail().wallet_amount
                            )
                        )
                    }
            }
            else
            {
                txt_title!!.setText(getString(R.string.account_balance))
                balance_layout!!.visibility = View.VISIBLE
            }
        } catch (e: IllegalStateException) {
            balance_layout!!.visibility = View.VISIBLE
        }
    }

    /*
    *  Calculate the loyalty
    * */
    fun ComputeLoyalty(loyalty: Loyalty): ArrayList<String> {
        val drawableArray: ArrayList<String> = ArrayList();

        // filled cup
        for (i in 0 until loyalty.filledItems) {
            drawableArray.add(loyalty.filledItemImg)
        }

        // empty cup
        var no_of_empty_cup = loyalty.totalItems - (loyalty.filledItems + loyalty.freeItems)
        for (i in 0 until no_of_empty_cup) {
            drawableArray.add(loyalty.totalItemImg)
        }

        // free cup
        for (i in 0 until loyalty.freeItems) {
            drawableArray.add(loyalty.freeItemImg)
        }
        return drawableArray
    }

    override fun onBackPressed() {

        val fragment =
            this.supportFragmentManager.getPrimaryNavigationFragment();
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
      val childFragments = navHostFragment.childFragmentManager.fragments

        if(childFragments.size>0)
        {
            if (childFragments[0]  !is IOnBackPressed || !(childFragments[0]  as IOnBackPressed).onBackPressed()) {
                super.onBackPressed()
            }
        }
        else{
            super.onBackPressed()
        }
    }
}

