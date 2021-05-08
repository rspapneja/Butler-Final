package com.butlerschocolates.app.fragment.order

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.activities.PaymentWebViewActivity
import com.butlerschocolates.app.adapter.cart.CartProductAdapter
import com.butlerschocolates.app.adapter.savedCard.SavedCardAdapter
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.database.DatabaseHelper
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.OptionAttribute
import com.butlerschocolates.app.database.dbmodel.ProductOptionItem
import com.butlerschocolates.app.databinding.FragmentOrderPaymentBinding
import com.butlerschocolates.app.dialog.BillingdAddressDialog
import com.butlerschocolates.app.dialog.FreeProductDialog
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.loyality.chkloyalty.CheckLoyaltyRequestBody
import com.butlerschocolates.app.model.orderInfo.OrderInfoRequestBody
import com.butlerschocolates.app.model.payment.PaymentRequestBody
import com.butlerschocolates.app.model.storetiming.SavedCards
import com.butlerschocolates.app.model.storetiming.StoreTimingRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation
import com.butlerschocolates.app.viewmodels.payment.PaymentVewModel
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard


class OrderPaymentFragment : BaseFragment(), View.OnClickListener,
    SavedCardAdapter.SavedCardClickedListener, CartProductAdapter.CartProductClickedListener,
    FreeProductDialog.FreeProductListener, BillingdAddressDialog.BillingAddressListerner {

    var binding: FragmentOrderPaymentBinding? = null

    var displayProductCartArray: ArrayList<ProductOptionItem>? = null
    var optionAttribute: ArrayList<OptionAttribute>? = null

    var totalCarPrice: Double = 0.0

    var currencySymbol: String = ""
    var cartProductAdapter: CartProductAdapter? = null
    var viewModel: PaymentVewModel? = null

    var isPaymentType = 0  // 0 for Butler card, 1 for cardPayment
    var isSavedCardSelected = 0  //0 for new Card, 1 for Saved Card

    var isAddCardLayoutVisible = 0  //0 for not visible(hide) , 1 for visible

    var timeSlotArray: ArrayList<String>? = ArrayList()
    var isSlectedTimeSlot: Boolean = false

    var mSelectedIndex = 0
    var savedCardArray: ArrayList<SavedCards>? = null
    var carId = 0

    var selectedSavedCardPos = 0
    var storeID = ""
    var storeTitle = ""
    var TAG = "Tag Order Payment"

    var orderId: String = ""

    var loyaltyMessage: String = ""
    var loyaltyTitle: String = ""
    var loyaltyFreeValue: Int = 0
    var reedmNo = 0
    var discount = 0.00

    var billingdAddressDialog:BillingdAddressDialog?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_payment, container, false)
        val view: View = binding!!.getRoot()

        setInitailValue()

        getAllProducts()

        setupCartProductPrecyclerView()
        setTotalCartPrice()
        setPaymentViewModel()
        getStoreTiming()

        checkWalletAmout()

        selectedPaymentType()
        setupListerner()


        return view
    }

    private fun checkWalletAmout() {

        if (utilities!!.getCustomerDetail().syncWithButlers == 0) {
            binding!!.tvButlerCard.setAlpha(0.5F)
            binding!!.tvButlerCard.isEnabled = false

            isPaymentType = 1
        } else {
            binding!!.tvButlerCard.isEnabled = true

            if (utilities!!.getCustomerDetail().wallet_amount >= calculateTotalCartPrice()) {
                isPaymentType = 0
            } else {
                isPaymentType = 1
            }
        }
    }

    private fun setInitailValue() {
        timeSlotArray = ArrayList()
        timeSlotArray!!.clear()
        isSlectedTimeSlot = false

        isPaymentType = 0
        isSavedCardSelected = 0
        isAddCardLayoutVisible = 0
        mSelectedIndex = 0
        selectedSavedCardPos = 0
        carId = 0
    }

    private fun setPaymentViewModel() {
        viewModel = ViewModelProviders.of(this).get(PaymentVewModel::class.java)
        viewModel!!.init()
        binding!!.viewModel = viewModel
    }

    private fun setBannerInfo(bannerImage:String,bannerTitle:String) {
        utilities!!.loadImage(bannerImage,binding!!.bannerimage)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding!!.tvMyOrder.setText(Html.fromHtml(bannerTitle, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding!!.tvMyOrder.setText(Html.fromHtml(bannerTitle));
        }
    }

    private fun selectedPaymentType() {
        if (isPaymentType == 0) {
            binding!!.tvButlerCard.setBackgroundResource(R.drawable.round_rectangular_pantone_light)
            binding!!.tvCardPayment.setBackgroundResource(R.drawable.round_rectangular_sea_dark)
            binding!!.tvPayNow.visibility = View.VISIBLE
            binding!!.addCardLayout.visibility = View.GONE
        } else {
            binding!!.tvButlerCard.setBackgroundResource(R.drawable.round_rectangular_sea_dark)
            binding!!.tvCardPayment.setBackgroundResource(R.drawable.round_rectangular_pantone_light)
            binding!!.tvPayNow.visibility = View.GONE
            binding!!.addCardLayout.visibility = View.VISIBLE

            if (isAddCardLayoutVisible == 1) {
                binding!!.hideShowCardLayout.visibility = View.VISIBLE
                binding!!.imgHideShowCardButton.setRotation(0F)
            } else {
                binding!!.hideShowCardLayout.visibility = View.GONE
                binding!!.imgHideShowCardButton.setRotation(180F)
            }

            if (isSavedCardSelected == 1) {
                binding!!.savedCardsBtn.setBackgroundResource(R.drawable.round_rectangular_pantone_light)
                binding!!.newCardBtn.setBackgroundResource(R.drawable.round_rectangular_sea_dark)
                binding!!.newCardIncludeLayout.newCardLayout.visibility = View.GONE
                binding!!.savedCardIncludeLayout.savedCardLayout.visibility = View.VISIBLE
            } else {
                binding!!.savedCardsBtn.setBackgroundResource(R.drawable.round_rectangular_sea_dark)
                binding!!.newCardBtn.setBackgroundResource(R.drawable.round_rectangular_pantone_light)
                binding!!.newCardIncludeLayout.newCardLayout.visibility = View.VISIBLE
                binding!!.savedCardIncludeLayout.savedCardLayout.visibility = View.GONE
            }
        }
    }

    fun getStoreTiming() {
        viewModel!!.ontStoreTimingRequest(requireActivity(), createTimingRequestBody())
            .observe(viewLifecycleOwner, Observer {

                // hide progress loading
                viewModel!!.setIsLoading(false, requireActivity())

                // store Timming
                if (it!!.getData() != null) {
                    if (it!!.getData().code == 1||it!!.getData().code == 6) {
                        for (i in 0..it!!.getData().data!!.timings!!.size - 1) {
                            for (j in 0..it!!.getData().data!!.timings!!.get(i).time.size - 1) {
                                timeSlotArray!!.add(
                                    it!!.getData().data!!.timings!!.get(i).day + " @ " + it!!.getData().data!!.timings!!.get(
                                        i
                                    ).time.get(j)
                                )
                            }
                        }

                        if (it!!.getData().data!!.timings!!.size > 0) {
                            timeSlotArray?.add(0, "Select Day @ Time");
                            setTimeSlotOnSpinner()
                        } else {
                            var message = ""
                            if (it!!.getData().data!!.error != null)
                                message = it!!.getData().data!!.error
                            else
                                message =
                                    "Store is not available at this moment.Please try afer some time later"

                            utilities!!.showAlertWithLayoutId(
                                "Alert",
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
                                        (activity as MainActivity).onBackPressed()
                                    }
                                })
                        }

                        // get Saved Card array list
                        savedCardArray = it!!.getData().data!!.savedCards!! as ArrayList<SavedCards>

                        // Loyalty
                        if (it!!.getData().data!!.loyalty_free > 0) {
                            binding!!.getLoyaltyLayout.visibility = View.VISIBLE
                            binding!!.cancelFreeCoffeeLayout.visibility = View.GONE
                            binding!!.bottomLine.visibility = View.VISIBLE
                        }


                        binding!!.tvLoyaltyTitle.text = it!!.getData().data!!.loyalty_section_title
                        loyaltyTitle = it!!.getData().data!!.loyalty_section_title
                        loyaltyMessage = it!!.getData().data!!.loyalty_popup_msg
                        loyaltyFreeValue = it!!.getData().data!!.loyalty_free

                        // banner Image
                        setBannerInfo(it!!.getData().data!!.banner_img,it!!.getData()!!.data.banner_title)
                      //  setBannerTitle()

                    }
                   else if (it!!.getData().code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(it!!.getData().data.error)
                    }
                    else if(it!!.getData().code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                }
                else {
                    // api call failed.
                    utilities!!.showAlert("Error", utilities!!.apiAlert(it.error))
                }
            })
    }


    private fun setTimeSlotOnSpinner() {

        val mAdapter =
            object :
                ArrayAdapter<String>(requireContext(), R.layout.view_time_slot, timeSlotArray!!) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val tv = super.getView(position, convertView, parent) as TextView
                    tv.setTextColor(Color.WHITE)
                    return tv
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val tv = super.getDropDownView(position, convertView, parent) as TextView
                    tv.setTextColor(context.resources.getColor(R.color.txtPantoneBlack))
                    return tv
                }

                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }
            }

        // Data bind the spinner with array adapter items
        binding!!.spinnerTimeslot?.adapter = mAdapter

        // Set an item selection listener for spinner widget
        binding!!.spinnerTimeslot?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    // Set the value for selected index variable
                    mSelectedIndex = i

                    if (isSlectedTimeSlot) {
                        val arrOfStr: List<String> =
                            binding!!.spinnerTimeslot!!.getItemAtPosition(i).toString().split("@")
                        binding!!.tvDay.setText(arrOfStr.get(0))
                        binding!!.tvTime.setText(arrOfStr.get(1))
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                }
            }
    }


    private fun setTotalCartPrice() {
        binding!!.totalCartPrice.setText(
            currencySymbol + String.format(
                "%.2f",
                calculateTotalCartPrice() - discount
            )
        )
    }

    private fun calculateTotalCartPrice(): Double {
        var totalProductPrice = 0.0
        var totalOptionPrice = 0.0
        totalCarPrice = 0.0

        for (i in 0..displayProductCartArray!!.size - 1) {
            totalProductPrice = displayProductCartArray!!.get(i).price
            totalOptionPrice = 0.0

            for (j in 0..displayProductCartArray!!.get(i).optionItem.size - 1) {
                totalOptionPrice =
                    totalOptionPrice + displayProductCartArray!!.get(i).optionItem.get(j).option_price
            }
            totalCarPrice =
                totalCarPrice + (totalOptionPrice + totalProductPrice) * displayProductCartArray!!.get(
                    i
                ).product_qty
        }

        return totalCarPrice
    }

    private fun getAllProducts() {
        val productCursor = databaseHelper!!.selectProducts()
        displayProductCartArray = ArrayList<ProductOptionItem>()

        while (productCursor.moveToNext()) {
            var optionsCursor = databaseHelper!!.selectProductOptions(
                productCursor.getInt(
                    productCursor.getColumnIndex("cart_id")
                )
            )
            currencySymbol = productCursor.getString(productCursor.getColumnIndex("cur_symbol"))
            storeID = productCursor.getString(productCursor.getColumnIndex("store_id"))
            storeTitle = productCursor.getString(productCursor.getColumnIndex("store_title"))

            optionAttribute = ArrayList<OptionAttribute>()

            if (productCursor.getInt(productCursor.getColumnIndex("productOptionsSize")) >= 1) {
                while (optionsCursor.moveToNext()) {
                    var optionsItemCursor = databaseHelper!!.selectOptionsItem(
                        optionsCursor.getInt(
                            optionsCursor.getColumnIndex("option_id")
                        )
                    )
                    while (optionsItemCursor.moveToNext()) {
                        optionAttribute!!.add(
                            OptionAttribute(
                                optionsItemCursor.getInt(optionsItemCursor.getColumnIndex("attribute_id")),
                                optionsItemCursor.getInt(optionsItemCursor.getColumnIndex("patt_id")),
                                optionsItemCursor.getDouble(optionsItemCursor.getColumnIndex("option_price")),
                                optionsItemCursor.getString(optionsItemCursor.getColumnIndex("option_value"))
                            )
                        )
                    }
                }
            }

            var is_complementary =
                productCursor.getInt(productCursor.getColumnIndex("ComplementaryProductStatus"))
            var complementaryItemsList = ArrayList<ComplementaryItemsDB>()

            if (is_complementary == 1) {
                var complementaryItemCursor = databaseHelper!!.selectComplementaryItemswithId(
                    productCursor.getInt(productCursor.getColumnIndex("cart_id"))
                )

                while (complementaryItemCursor.moveToNext()) {
                    complementaryItemsList.add(
                        ComplementaryItemsDB(
                            productCursor.getInt(productCursor.getColumnIndex("cart_id")),
                            productCursor.getInt(productCursor.getColumnIndex("product_id")),
                            -1,  // -1 means value not fetch  from complemtary_info table as there is no need of at this moment
                            -1, // -1 means value not fetch  from complemtary_info table as there is no need of at this moment
                            complementaryItemCursor!!.getString(
                                complementaryItemCursor.getColumnIndex(
                                    "value"
                                )
                            ),
                            complementaryItemCursor!!.getInt(
                                complementaryItemCursor.getColumnIndex(
                                    "value_id"
                                )
                            ),
                            complementaryItemCursor!!.getString(
                                complementaryItemCursor.getColumnIndex(
                                    "image"
                                )
                            )
                        )
                    )
                }
            }

            // add product
            displayProductCartArray!!.add(
                ProductOptionItem(
                    productCursor.getInt(productCursor.getColumnIndex("cart_id")),
                    productCursor.getString(productCursor.getColumnIndex("product_title")),
                    productCursor.getInt(productCursor.getColumnIndex("product_id")),
                    productCursor.getInt(productCursor.getColumnIndex("category_id")),
                    productCursor.getInt(productCursor.getColumnIndex("product_qty")),
                    productCursor.getInt(productCursor.getColumnIndex("product_max_qty")),
                    productCursor.getInt(productCursor.getColumnIndex("subCategoryId")),
                    productCursor.getString(productCursor.getColumnIndex("productImage")),
                    productCursor.getInt(productCursor.getColumnIndex("store_id")),
                     productCursor.getString(productCursor.getColumnIndex("store_title")),
                    productCursor.getString(productCursor.getColumnIndex("description")),
                    productCursor.getDouble(productCursor.getColumnIndex("price")),
                    productCursor.getInt(productCursor.getColumnIndex("productOptionsSize")),
                    productCursor.getString(productCursor.getColumnIndex("cur_symbol")),
                    optionAttribute!!,
                    complementaryItemsList
                )
            )
        }
    }

    private fun setupCartProductPrecyclerView() {
        var layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.productCartRecycleview.setLayoutManager(layoutManager)

        cartProductAdapter = CartProductAdapter(
            requireContext(),
            displayProductCartArray!! as ArrayList<ProductOptionItem>, 0, this
        )
        binding!!.productCartRecycleview!!.setAdapter(cartProductAdapter)
    }

    // return Timing Request body
    private fun createTimingRequestBody(): StoreTimingRequestBody {
        var storeOptionsTmingRequestBody: ArrayList<StoreTimingRequestBody.ProductRequestBody.OptionsRequestBody>? =
            null
        var storeProductTimingRequestBody: ArrayList<StoreTimingRequestBody.ProductRequestBody>? =
            ArrayList()

        val productCursor = databaseHelper!!.selectProducts()

        while (productCursor.moveToNext()) {
            storeOptionsTmingRequestBody = ArrayList()
            var optionsItemCursor = databaseHelper!!.selectOptionsItemWithCarID(
                productCursor.getInt(
                    productCursor.getColumnIndex("cart_id")
                )
            )
            while (optionsItemCursor.moveToNext()) {
                if (productCursor.getInt(productCursor.getColumnIndex("cart_id")) == optionsItemCursor.getInt(
                        optionsItemCursor.getColumnIndex("cart_id")
                    )
                ) {
                    storeOptionsTmingRequestBody!!.add(
                        StoreTimingRequestBody.ProductRequestBody.OptionsRequestBody(
                            optionsItemCursor.getInt(optionsItemCursor.getColumnIndex("patt_id"))
                        )
                    )
                }
            }
            storeProductTimingRequestBody!!.add(
                StoreTimingRequestBody.ProductRequestBody(
                    storeOptionsTmingRequestBody!!, productCursor.getInt(
                        productCursor.getColumnIndex("product_id")
                    ), productCursor.getInt(
                        productCursor.getColumnIndex("product_qty")
                    )
                )
            )
        }
        var requestBody =
            StoreTimingRequestBody(
                utilities!!.readPref("Auth_Token"),
                storeProductTimingRequestBody!!,
                AppConstants.API_VERSION,
                storeID
            )
        return requestBody
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_butlerCard -> {
                isPaymentType = 0
                selectedPaymentType()
                scrollToBottom()
            }
            R.id.tv_cardPayment -> {
                isPaymentType = 1
                selectedPaymentType()
                scrollToBottom()
            }
            R.id.tv_payNow -> {

                if (utilities!!.getCustomerDetail().wallet_amount >= binding!!.totalCartPrice.text.substring(1,binding!!.totalCartPrice.text.length).toDouble()) {
                    if (isSlectedTimeSlot) {
                        callPaymentAPi("loyaltycard","","","","")
                    } else {
                        utilities!!.showAlert("Select", getString(R.string.select_time_day))
                    }
                } else
                    utilities!!.showAlert(
                        "Payment",
                        "Wallet balance is not sufficient. Proceed with card payment"
                    )
            }
            R.id.storetimeslotLayout -> {
                isSlectedTimeSlot = true
                binding!!.spinnerTimeslot?.performClick()
            }
            R.id.img_hideShowCardButton -> {
                if(savedCardArray!=null) {
                    if (savedCardArray!!.size > 0) {
                        isSavedCardSelected = 1
                        setSavedCardsAdapter()
                    } else {
                        isSavedCardSelected = 0
                    }
                }
                else{
                    isSavedCardSelected = 0
                }

                if (isAddCardLayoutVisible == 0) {
                    isAddCardLayoutVisible = 1
                    selectedPaymentType()
                } else {
                    isAddCardLayoutVisible = 0
                    selectedPaymentType()
                }
            }

            R.id.savedCardsBtn -> {
                if( savedCardArray!=null) {
                    if (savedCardArray!!.size == 0) {
                        utilities!!.showAlert("Card", "No Saved Card available yet")
                    } else if (savedCardArray!!.size > 0) {
                        isSavedCardSelected = 1
                        setSavedCardsAdapter()
                        selectedPaymentType()
                    }
                }
                else{
                    utilities!!.showAlert("Card", "No Saved Card available yet")
                }
            }
            R.id.newCardBtn -> {
                isSavedCardSelected = 0
                selectedPaymentType()
            }
            R.id.bt_newCardPayNow -> {
                carId = 0
                var validation = Validation(requireContext(), utilities!!)
                if (isSlectedTimeSlot) {
                    if (validation.cardValidate(
                            binding!!.newCardIncludeLayout!!.newCardName,
                            binding!!.newCardIncludeLayout!!.newCardNumber,
                            binding!!.newCardIncludeLayout!!.newCardExpiry,
                            binding!!.newCardIncludeLayout!!.newCardCvv
                        )
                    ) {
                         billingdAddressDialog=BillingdAddressDialog(context,this)

                            billingdAddressDialog!!.showAddressPopup(
                            "",
                            "",
                            "",
                            ""
                            ,"NewCard"
                        )
                    }
                } else {
                    utilities!!.showAlert("Select", getString(R.string.select_time_day))
                }
            }

            R.id.bt_savedCardPayNow -> {
                if (isSlectedTimeSlot) {
                    binding!!.newCardIncludeLayout!!.newCardName.setText("")
                    binding!!.newCardIncludeLayout!!.newCardNumber.setText("")
                    binding!!.newCardIncludeLayout!!.newCardExpiry.setText("")
                    carId = savedCardArray!!.get(selectedSavedCardPos).card_id.toInt()

                    if (binding!!.savedCardIncludeLayout!!.savedCardCvv.text.toString()
                            .isNotEmpty()
                    ) {

                        billingdAddressDialog=BillingdAddressDialog(context,this)
                        billingdAddressDialog!!.showAddressPopup(
                            savedCardArray!!.get(selectedSavedCardPos).billing.address,
                            savedCardArray!!.get(selectedSavedCardPos).billing.city,
                            savedCardArray!!.get(selectedSavedCardPos).billing.country,
                            savedCardArray!!.get(selectedSavedCardPos).billing.postalcode
                            ,"SavedCard"
                        )
                    }
                    else {
                        utilities!!.showAlert("Card", "Please Enter the CVV No")
                    }

                } else {
                    utilities!!.showAlert("Error", getString(R.string.select_time_day))
                }
            }
            R.id.scanCard -> {
                scanCreditCard()
            }
            R.id.availFreeCoffee -> {
                FreeProductDialog(
                    requireActivity(),
                    loyaltyTitle,
                    loyaltyMessage,
                    loyaltyFreeValue,
                    this
                ).showFreeProductDialog()
            }
            R.id.cancelFreeCoffee -> {
                reedmNo = 0
                discount = 0.00
                binding!!.cancelFreeCoffeeLayout.visibility = View.GONE
                binding!!.getLoyaltyLayout.visibility = View.VISIBLE
                binding!!.paymentOptionLayout.visibility = View.VISIBLE
                setTotalCartPrice()
                selectedPaymentType()
            }
        }
    }

    private fun setSavedCardsAdapter() {
        var layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.savedCardIncludeLayout!!.recyclerSavedCard.setLayoutManager(layoutManager)

        savedCardArray!!.get(selectedSavedCardPos).isSelected = 1

        // category adpter
        var savedCardAdapter =
            SavedCardAdapter(
                requireActivity(),
                savedCardArray!!,
                this
            )
        binding!!.savedCardIncludeLayout!!.recyclerSavedCard!!.setAdapter(savedCardAdapter)
    }

    private fun scanCreditCard() {
        val scanIntent: Intent = Intent(activity, CardIOActivity::class.java);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(
            CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE,
            false
        ); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, GlobalConstants.REQUEST_CODE_CARD_SCAN)
    }

    private fun callPaymentAPi(payMode: String,billing_address:String,billing_city:String,billing_county:String,billing_postal_code:String) {
        viewModel!!.paymentInit()

        viewModel!!.onCardPaymentRequest(requireActivity(), createCartPaymentRequestBody(payMode,billing_address,billing_city,billing_county,billing_postal_code))!!
            .observe(
                requireActivity(), androidx.lifecycle.Observer {

                    // hide progress loading
                    viewModel!!.setIsLoading(false, requireActivity())
                    if (it!!.getData() != null) {
                        if (it!!.getData().code == 6) {
                            orderId = it!!.getData().data!!.redirect.orderId!!.toString()

                            var intent =
                                Intent(requireActivity(), PaymentWebViewActivity::class.java)
                            intent.putExtra("loadUrl", it!!.getData().data!!.redirect.redirectUrl)
                            startActivityForResult(
                                intent,
                                GlobalConstants.RESULT_CODE_SECURE_PAYMENT
                            )
                        } else {
                            if (payMode.equals("loyaltycard")) {
                                utilities?.saveCustomerDetail(it!!.getData().data!!.customer!!)
                                (activity as MainActivity).hideShowAccountBalance()
                            }
                            orderId = it!!.getData().data!!.success.orderId.toString()
                            openOrderStatusScreen()
                        }
                    } else {
                        // api call failed.
                        utilities!!.showAlert("Error", utilities!!.apiAlert(it.error))
                    }
                }
            )
    }

    private fun openOrderStatusScreen() {
        databaseHelper =
            DatabaseHelper(requireContext())
        databaseHelper!!.emptyCart()

        GlobalConstants.OrderId = orderId.toInt()

        val navBuilder = NavOptions.Builder()
        val navOptions = navBuilder.setPopUpTo(R.id.orderPaymentFragment, true).build()
        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.orderStatusFragment, null, navOptions)
    }

    private fun setupListerner() {
        binding!!.tvButlerCard.setOnClickListener(this)
        binding!!.storetimeslotLayout.setOnClickListener(this)

        binding!!.tvPayNow.setOnClickListener(this)
        binding!!.tvButlerCard.setOnClickListener(this)
        binding!!.tvCardPayment.setOnClickListener(this)
        binding!!.imgHideShowCardButton.setOnClickListener(this)
        binding!!.savedCardsBtn.setOnClickListener(this)
        binding!!.newCardBtn.setOnClickListener(this)
        binding!!.newCardIncludeLayout.btNewCardPayNow.setOnClickListener(this)
        binding!!.newCardIncludeLayout.scanCard.setOnClickListener(this)
        binding!!.savedCardIncludeLayout.btSavedCardPayNow.setOnClickListener(this)
        binding!!.availFreeCoffee.setOnClickListener(this)
        binding!!.cancelFreeCoffee.setOnClickListener(this)
    }

    private fun createCartPaymentRequestBody(payMode: String,billing_address:String,billing_city:String,billing_county:String,billing_postal_code:String): PaymentRequestBody {
        var optionRequestBoday: ArrayList<PaymentRequestBody.ProductRequestBody.OptionsRequestBody>? =
            null
        var productRequestBody: ArrayList<PaymentRequestBody.ProductRequestBody>? = null
        var complementartlist: ArrayList<PaymentRequestBody.ProductRequestBody.Complementary>? =
            null
        productRequestBody = ArrayList()
        val productCursor = databaseHelper!!.selectProducts()

        while (productCursor.moveToNext()) {
            optionRequestBoday = ArrayList()
            var optionAttrtibutes = 0
            var optionsItemCursor = databaseHelper!!.selectOptionsItemWithCarID(
                productCursor.getInt(
                    productCursor.getColumnIndex("cart_id")
                )
            )
            while (optionsItemCursor.moveToNext()) {
                if (productCursor.getInt(productCursor.getColumnIndex("cart_id")) == optionsItemCursor.getInt(
                        optionsItemCursor.getColumnIndex("cart_id")
                    )
                ) {
                    optionAttrtibutes = optionAttrtibutes + optionsItemCursor.getInt(
                        optionsItemCursor.getColumnIndex("option_price")
                    )
                    optionRequestBoday!!.add(
                        PaymentRequestBody.ProductRequestBody.OptionsRequestBody(
                            optionsItemCursor.getInt(optionsItemCursor.getColumnIndex("patt_id"))
                        )
                    )
                }
            }

            var totalPrice = String.format(
                "%.2f",
                ((productCursor.getInt(productCursor.getColumnIndex("product_qty")) * (productCursor.getDouble(
                    productCursor.getColumnIndex("price")
                ) + optionAttrtibutes).toDouble()))
            ).toDouble() - discount


            var is_complementary =
                productCursor.getInt(productCursor.getColumnIndex("ComplementaryProductStatus"))
            complementartlist = ArrayList()

            if (is_complementary == 1) {
                var complementaryItemCursor = databaseHelper!!.selectComplementaryItemswithId(
                    productCursor.getInt(productCursor.getColumnIndex("cart_id"))
                )
                while (complementaryItemCursor.moveToNext()) {
                    complementartlist!!.add(
                        PaymentRequestBody.ProductRequestBody.Complementary(
                            complementaryItemCursor!!.getInt(
                                complementaryItemCursor.getColumnIndex(
                                    "value_id"
                                )
                            )
                        )
                    )
                }
            }

            productRequestBody!!.add(
                PaymentRequestBody.ProductRequestBody(
                    totalPrice,
                    optionRequestBoday!!,
                    productCursor.getInt(productCursor.getColumnIndex("product_id")),
                    productCursor.getInt(productCursor.getColumnIndex("product_qty")),
                    complementartlist
                )
            )
        }

        if (payMode.equals("creditcard")) {
            var cvv = ""
            if (carId == 0)
                cvv = binding!!.newCardIncludeLayout.newCardCvv!!.text.toString()
            else
                cvv = binding!!.savedCardIncludeLayout.savedCardCvv!!.text.toString()

            var requestBody =
                PaymentRequestBody(
                    utilities!!.readPref("Auth_Token"),
                    storeID,
                    binding!!.newCardIncludeLayout.newCardExpiry!!.text.toString().replace("/", ""),
                    binding!!.newCardIncludeLayout.newCardName!!.text.toString(),
                    carId,
                    binding!!.newCardIncludeLayout.newCardNumber!!.text.toString().replace(" ", ""),
                    cvv,
                    if (binding!!.newCardIncludeLayout.newCardCheckBoxButton.isChecked) 1 else 0,
                    binding!!.tvDay.text.toString().trim(),
                    binding!!.tvTime.text.toString().trim(),
                    productRequestBody!!,
                    AppConstants.API_VERSION,
                    payMode,
                    reedmNo,
                    billing_address,
                    billing_county,
                    billing_city,
                    billing_postal_code
                )
            return requestBody
        } else {
            var requestBody =
                PaymentRequestBody(
                    utilities!!.readPref("Auth_Token"),
                    storeID,
                    "",
                    "",
                    0,
                    "",
                    "",
                    -1,
                    binding!!.tvDay.text.toString().trim(),
                    binding!!.tvTime.text.toString().trim(),
                    productRequestBody!!,
                    AppConstants.API_VERSION,
                    payMode,
                    reedmNo,
                    billing_address,
                    billing_county,
                    billing_city,
                    billing_postal_code
                )
            return requestBody
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GlobalConstants.RESULT_CODE_SECURE_PAYMENT) {
            getOrderDetail(data!!.getStringExtra("paymentStatus"));
        } else if (requestCode == GlobalConstants.REQUEST_CODE_CARD_SCAN) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult: CreditCard =
                    data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)!!
                binding!!.newCardIncludeLayout.newCardNumber!!.setText(
                    scanResult.formattedCardNumber.replace(
                        " ",
                        ""
                    )
                )
                if (scanResult.isExpiryValid) {
                    binding!!.newCardIncludeLayout.newCardExpiry!!.setText(
                        String.format("%02d",scanResult.expiryMonth) + "/" + scanResult.expiryYear.toString()
                            .substring(2)
                    )
                }
                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    binding!!.newCardIncludeLayout.newCardCvv!!.setText(scanResult.cvv)
                }
            }
        }
    }

    override fun onSavedCardSelected(pos: Int) {
        selectedSavedCardPos = pos
    }

    fun getOrderDetail(paymentStatus:String?) {
        viewModel!!.getOrderInfo(creatteOrderInfoRequestBody(paymentStatus!!))
            .observe(requireActivity(), Observer { orderInfoData ->
                when (orderInfoData!!.status) {
                    Status.LOADING -> {
                        viewModel!!.setIsLoading(true, requireActivity())
                    }
                    Status.SUCCESS -> {
                        openOrderStatusScreen()
                        viewModel!!.setIsLoading(false, requireActivity())
                    }
                    Status.ERROR -> {
                        viewModel!!.setIsLoading(false, requireActivity())
                        utilities!!.showAlert(
                            "Error",
                            utilities!!.apiAlert(orderInfoData.throwable!!)
                        )
                    }
                }
            })
    }

    private fun creatteOrderInfoRequestBody(paymentStatus:String): OrderInfoRequestBody {
        var requestBody = OrderInfoRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.order_id = orderId.toInt()
        requestBody.check_payment = 1
        requestBody.webview_return = paymentStatus

        return requestBody
    }

    fun checkLoyalty(freeProductQuantity: Int, isDeleteCartButtonPressed: Boolean) {
        viewModel!!.checkLoyalty(createCheckLoyaltyRequestBody(freeProductQuantity))
            .observe(requireActivity(), Observer { checkloyaltyData ->
                when (checkloyaltyData!!.status) {
                    Status.LOADING -> {
                        viewModel!!.setIsLoading(true, requireActivity())
                    }
                    Status.SUCCESS -> {
                        if (checkloyaltyData.data!!.code == 1) {
                            if (checkloyaltyData.data.data.loyaltyFree != 0) {
                                if (isDeleteCartButtonPressed) {
                                    binding!!.cancelFreeCoffeeLayout.visibility = View.GONE
                                    binding!!.getLoyaltyLayout.visibility = View.VISIBLE
                                    reedmNo = 0
                                } else {
                                    binding!!.cancelFreeCoffeeLayout.visibility = View.VISIBLE
                                    binding!!.getLoyaltyLayout.visibility = View.GONE
                                }
                            } else {
                                binding!!.cancelFreeCoffeeLayout.visibility = View.GONE
                                binding!!.getLoyaltyLayout.visibility = View.GONE
                            }

                            binding!!.tvDiscount.text =
                                currencySymbol + utilities!!.showTwoDecimalPos(checkloyaltyData.data.data.redeemLoyalty.discount)
                            binding!!.tvReedomNo.text = freeProductQuantity.toString()

                            loyaltyTitle = checkloyaltyData.data.data.loyaltySectionTitle
                            loyaltyMessage = checkloyaltyData.data.data.loyaltyPopupMsg
                            loyaltyFreeValue = checkloyaltyData.data.data.loyaltyFree

                            binding!!.tvLoyaltyTitle.text = checkloyaltyData.data.data.loyaltySectionTitle

                            binding!!.totalCartPrice.text =
                                currencySymbol + utilities!!.showTwoDecimalPos((calculateTotalCartPrice() - checkloyaltyData.data.data.redeemLoyalty.discount))
                            discount = checkloyaltyData.data.data.redeemLoyalty.discount

                            if (calculateTotalCartPrice() - checkloyaltyData.data.data.redeemLoyalty.discount <= 0.00) {
                                binding!!.paymentOptionLayout.visibility = View.GONE
                                binding!!.totalCartPrice.text = currencySymbol + "0.00"
                                binding!!.tvPayNow.visibility=View.VISIBLE
                                binding!!.addCardLayout.visibility=View.GONE
                            }

                        } else
                            utilities!!.showAlert("Error", checkloyaltyData!!.data!!.data.error)

                        viewModel!!.setIsLoading(false, requireActivity())
                    }
                    Status.ERROR -> {
                        viewModel!!.setIsLoading(false, requireActivity())
                        utilities!!.showAlert(
                            "Error",
                            utilities!!.apiAlert(checkloyaltyData.throwable!!)
                        )
                    }
                }
            })
    }


    private fun createCheckLoyaltyRequestBody(freeProductQuantity: Int): CheckLoyaltyRequestBody {
        var storeOptionsTmingRequestBody: ArrayList<CheckLoyaltyRequestBody.ProductRequestBody.OptionsRequestBody>? =
            null
        var storeProductTimingRequestBody: ArrayList<CheckLoyaltyRequestBody.ProductRequestBody>? =
            ArrayList()
        var complementartlist: ArrayList<CheckLoyaltyRequestBody.ProductRequestBody.Complementary>? =
            null
        val productCursor = databaseHelper!!.selectProducts()

        while (productCursor.moveToNext()) {
            storeOptionsTmingRequestBody = ArrayList()
            var optionsItemCursor = databaseHelper!!.selectOptionsItemWithCarID(
                productCursor.getInt(
                    productCursor.getColumnIndex("cart_id")
                )
            )
            while (optionsItemCursor.moveToNext()) {
                if (productCursor.getInt(productCursor.getColumnIndex("cart_id")) == optionsItemCursor.getInt(
                        optionsItemCursor.getColumnIndex("cart_id")
                    )
                ) {
                    storeOptionsTmingRequestBody!!.add(
                        CheckLoyaltyRequestBody.ProductRequestBody.OptionsRequestBody(
                            optionsItemCursor.getInt(optionsItemCursor.getColumnIndex("patt_id"))
                        )
                    )
                }
            }

            var is_complementary =
                productCursor.getInt(productCursor.getColumnIndex("ComplementaryProductStatus"))
            complementartlist = ArrayList()

            if (is_complementary == 1) {
                var complementaryItemCursor = databaseHelper!!.selectComplementaryItemswithId(
                    productCursor.getInt(productCursor.getColumnIndex("cart_id"))
                )

                while (complementaryItemCursor.moveToNext()) {
                    complementartlist!!.add(
                        CheckLoyaltyRequestBody.ProductRequestBody.Complementary(
                            complementaryItemCursor!!.getInt(
                                complementaryItemCursor.getColumnIndex(
                                    "value_id"
                                )
                            )
                        )
                    )
                }
            }

            storeProductTimingRequestBody!!.add(
                CheckLoyaltyRequestBody.ProductRequestBody(
                    storeOptionsTmingRequestBody!!, productCursor.getInt(
                        productCursor.getColumnIndex("product_id")
                    ), productCursor.getInt(
                        productCursor.getColumnIndex("product_qty")
                    )
                )
            )
        }
        var requestBody =
            CheckLoyaltyRequestBody(
                utilities!!.readPref("Auth_Token"),
                storeProductTimingRequestBody!!,
                AppConstants.API_VERSION,
                storeID,
                complementartlist!!
                , freeProductQuantity
            )
        return requestBody
    }

    fun scrollToBottom() {
        binding!!.nestedScrollView.post(Runnable { binding!!.nestedScrollView.fullScroll(View.FOCUS_DOWN) })
    }

    override fun deleteCartProduct(pos: Int)
    {
        databaseHelper!!.deleteProduct(displayProductCartArray!!.get(pos).cartId)
        displayProductCartArray!!.removeAt(pos)
        cartProductAdapter!!.notifyDataSetChanged()
        discount = 0.00
        setTotalCartPrice()

        if (reedmNo != 0 && displayProductCartArray!!.size != 0) {
            checkLoyalty(reedmNo, true)
        }

        if (displayProductCartArray!!.size == 0)
            (activity as MainActivity).onBackPressed()
    }

    override fun updateCartProductQty(pos: Int, actionType: String) {
        TODO("Not yet implemented")
    }

    override fun getFreeProduct(freeProductQuantity: Int) {
        reedmNo = freeProductQuantity
        checkLoyalty(freeProductQuantity, false)
    }

    override fun addBillingAddress(billing_address:String,billing_city:String,billing_county:String,billing_postal_code:String,actionType:String) {
         if(Validation(requireContext(),utilities!!).validateBillingAddress(billing_address,billing_city,billing_county,billing_postal_code))
         {
             billingdAddressDialog!!.closeDialog()
             callPaymentAPi("creditcard",billing_address,billing_city,billing_county,billing_postal_code)
         }
    }
}
