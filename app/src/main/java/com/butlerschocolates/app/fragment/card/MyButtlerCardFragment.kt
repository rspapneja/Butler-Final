package com.butlerschocolates.app.fragment.card

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.butlerschocolates.app.CupAdapter
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.BootStrapProcessActivity
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.activities.PaymentWebViewActivity
import com.butlerschocolates.app.adapter.savedCard.SavedCardAdapter
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentButtlerCardBinding
import com.butlerschocolates.app.dialog.BillingdAddressDialog
import com.butlerschocolates.app.dialog.LoyaltyCardNumberDialog
import com.butlerschocolates.app.dialog.LoyaltyLinkOptionDialog
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardRequestBody
import com.butlerschocolates.app.model.loyality.LoyalityRequestBody
import com.butlerschocolates.app.model.loyality.addLoyaltyCard.AddLoyalityCardRequestBody
import com.butlerschocolates.app.model.loyality.confirmTopupPayment.ConfirmTopupPaymentRequestBody
import com.butlerschocolates.app.model.storetiming.SavedCards
import com.butlerschocolates.app.model.syncloyalty.SyncLoyaltyRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation
import com.butlerschocolates.app.viewmodels.butlerCard.ButlerCardViewModel

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard


class MyButtlerCardFragment : BaseFragment(), View.OnClickListener,
    SavedCardAdapter.SavedCardClickedListener, BillingdAddressDialog.BillingAddressListerner,
    LoyaltyLinkOptionDialog.LoyaltyLinkOptionListener,
    LoyaltyCardNumberDialog.LoyaltyCardNumberListener {

    var binding: FragmentButtlerCardBinding? = null
    val TAG = "Tag MyButtlerCardFragment "

    val viewModel: ButlerCardViewModel by viewModels()

    var sheetBehavior: BottomSheetBehavior<*>? = null

    var isSavedCardSelected = 0  //0 for new Card, 1 for Saved Card

    var isAddCardLayoutVisible = 0  //0 for not visible(hide) , 1 for visible
    var savedCardsArraylist: ArrayList<SavedCards>? = ArrayList()

    var carId = 0

    var selectedSavedCardPos = 0
    var progressChangedValue = 0

    var payment_id = 0

    var billingdAddressDialog: BillingdAddressDialog? = null
    var loyaltyLinkOptionDialog: LoyaltyLinkOptionDialog? = null
    var loyaltyCardNumberDialog: LoyaltyCardNumberDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_buttler_card, container, false
        )

        val view: View = binding!!.getRoot()

        binding!!.qrCodeScanLayout.visibility=View.INVISIBLE
        binding!!.btnMyCode.visibility=View.INVISIBLE
        binding!!.btnScanCode.visibility=View.INVISIBLE

        selectedCardBottomSheet()
        checkLoginStatus()

        setBannerImage()
        setupViewModel()
        selectedPaymentType()
        qrCodeScanClick()
        setupListerner()
        getSavedCards()
        resetValue()
        makeClickableSpan()

        if(utilities!!.readPref("isLogin").equals("true"))
          setSeekBar()

        return view
    }

    private fun makeClickableSpan() {
        val ss = SpannableString("Loyalty card not yet linked \n Click here")
        val clickableSpan: ClickableSpan = object : ClickableSpan(),
            LoyaltyLinkOptionDialog.LoyaltyLinkOptionListener {

            override fun onClick(widget: View) {
                checkSyncLoyalty()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(true)
            }

            override fun onLinkOptionSelected(linkType: Int) {
                TODO("Not yet implemented")
            }
        }

        ss.setSpan(clickableSpan, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding!!.noCarLinked.text = ss
        binding!!.noCarLinked.movementMethod = LinkMovementMethod.getInstance()
        binding!!.noCarLinked.highlightColor = Color.TRANSPARENT
    }

    private fun resetValue() {
        isSavedCardSelected = 0
        isAddCardLayoutVisible = 0
        //selectedSavedCardPos=0
        carId = 0
    }

    private fun setSeekBar()  {
        binding!!.seekbarAmt.setMax(utilities!!.getHomeApiData().topup_seek_value.toInt())
        binding!!.tvAmount.setText(utilities!!.getCustomerDetail().currencySymbol +"0.00")

        binding!!.seekbarAmt.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                progressChangedValue = progress
                binding!!.tvAmount.setText(utilities!!.getCustomerDetail().currencySymbol +utilities!!.showTwoDecimalPos(progressChangedValue.toDouble()))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    private fun qrCodeScanClick() {
        binding!!.btnScanCode.setOnClickListener {
            checkSyncLoyalty()
        }
    }

    private fun setupListerner() {
        binding!!.bottomLayout.imgHideShowCardButton.setOnClickListener(this)
        binding!!.bottomLayout.savedCardsBtn.setOnClickListener(this)
        binding!!.bottomLayout.newCardBtn.setOnClickListener(this)
        binding!!.bottomLayout.newCardIncludeLayout.btNewCardPayNow.setOnClickListener(this)
        binding!!.bottomLayout.newCardIncludeLayout.scanCard.setOnClickListener(this)
        binding!!.bottomLayout.savedCardIncludeLayout.btSavedCardPayNow.setOnClickListener(this)
    }

    private fun selectedCardBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding!!.bottomLayout.bottomSheet);

        sheetBehavior!!.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                      //  hideShowSelectedCardLayout()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //hideShowSelectedCardLayout()
                    }
                }
            }
            override fun onSlide(view: View, p1: Float) {
            }
        })
        binding!!.bottomLayout.addCardLayout.setOnClickListener {
            resetValue()
            hideShowBottomSheet()
            hideShowSelectedCardLayout()
        }
    }

    private fun setupViewModel() {
        binding!!.viewModel = viewModel!!
    }

    private fun qrCodeScan() {
        var intentIntegrator: IntentIntegrator = IntentIntegrator.forSupportFragment(this)
        intentIntegrator.setDesiredBarcodeFormats(
            IntentIntegrator.QR_CODE,
            IntentIntegrator.CODE_128
        )
        intentIntegrator.setPrompt("Scan a barcode")
        intentIntegrator.setOrientationLocked(false)
        intentIntegrator.setCameraId(0)  // Use a specific camera of the device
        intentIntegrator.setBeepEnabled(false)
        intentIntegrator.setBarcodeImageEnabled(false)
        intentIntegrator.initiateScan()
    }

    private fun syncLoyalityPop() {
        if (utilities!!.getCustomerDetail().syncWithButlers == 0) {
            utilities!!.showAlertLoyaltyProgram(
                "Loyalty",
                utilities!!.getHomeApiData().register_loyalty_message,
                "YES",
                "NO",utilities!!.getHomeApiData().platinum_card_terms,
                object : Utilities.ActionButtons {
                    override fun okAction() {
                        syncLoyaltyRequest()
                    }
                    override fun cancelAction() {
                        (activity as MainActivity).onBackPressed()
                    }
                })
        }
    }

    private fun checkLoginStatus() {
        if (utilities!!.readPref("isLogin").equals("false")||utilities!!.readPref("isLogin").equals("")) {
            openLoginScreen()
        } else {
            syncLoyalityPop()
        }
    }

    private fun setBannerImage() {
        utilities!!.loadImage(utilities!!.getHomeApiData().topup_banner, binding!!.CardBannerImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 2) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            Console.Log(TAG, "" + result)
            if (result != null) {
                if (result.contents == null) {
                    Console.Log(TAG, "Cancelled")
                } else {
                    Console.Log(TAG, "Scanned: " + result.contents)

                    if(result.formatName.equals("QR_CODE"))
                    {
                        addLoyaltyCardApi(result.contents.toString())
                    }
                    else
                    {
                        utilities!!.showAlert("OR Code","Invalid QR Code")
                    }
                }
            }
        }
        if (requestCode == GlobalConstants.RESULT_CODE_SECURE_PAYMENT) {

             callConfirmTopupPayment(data!!.getStringExtra("paymentStatus"));
        }
        else if (requestCode == GlobalConstants.REQUEST_CODE_CARD_SCAN) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
           
                val scanResult: CreditCard =
                    data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)!!;
                binding!!.bottomLayout.newCardIncludeLayout.newCardNumber!!.setText(scanResult.formattedCardNumber.replace(" ", ""))

                if (scanResult.isExpiryValid) {
                    binding!!.bottomLayout.newCardIncludeLayout.newCardExpiry!!.setText(
                        String.format("%02d",scanResult.expiryMonth) + "/" + scanResult.expiryYear.toString()
                            .substring(2)
                    )
                }
                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    binding!!.bottomLayout.newCardIncludeLayout.newCardCvv!!.setText(scanResult.cvv)
                }
            }
        }
    }

    fun openLoginScreen() {
        val intent = Intent(activity, BootStrapProcessActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun syncLoyaltyRequest() {
        viewModel!!.syncLoyaltyRequest(createSyncLoyalityRequestBody())
        viewModel?.setIsLoading(true, requireActivity())

        viewModel!!.syncLoyaltyResponse.observe(this, Observer { data ->
            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel!!.setIsLoading(false, requireActivity())
                    if (data.data!!.code == 1) {

                        utilities?.saveCustomerDetail(data.data.data.customer!!)
                        (activity as MainActivity).hideShowAccountBalance()

                        utilities!!.showAlert("Success", data!!.data!!.data.success)
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else if(data.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else
                        utilities!!.showAlert("Error", data!!.data!!.data.error)
                }
                Status.ERROR -> {
                    viewModel!!.setIsLoading(false, requireActivity())
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
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

    private fun callloyaltyApi(
        billing_address: String,
        billing_city: String,
        billing_county: String,
        billing_postal_code: String
    ) {
        viewModel!!.loyalty(createLoyalityRequestBody( billing_address,billing_city,billing_county,billing_postal_code))
        //viewModel!!.setIsLoading(true, requireActivity())
        progressbar!!.show()

        viewModel!!.loyaltyResponse.observe(viewLifecycleOwner, Observer { data ->
            when (data!!.status) {
                Status.SUCCESS -> {
                   // viewModel!!.setIsLoading(false, requireActivity())
                    progressbar!!.hide()

                    if (data!!.data!!.code == 1) {

                      //  utilities!!.showAlert("Alert", data!!.data!!.data.success)
                        utilities!!.saveCustomerDetail(data.data!!.data.customer!!)
                        (activity as MainActivity).hideShowAccountBalance()

                        showTopScuccessAlert(data!!.data!!.data.success)

                        binding!!.bottomLayout.newCardIncludeLayout!!.newCardName.setText("")
                        binding!!.bottomLayout.newCardIncludeLayout.newCardNumber.setText("")
                        binding!!.bottomLayout.newCardIncludeLayout.newCardExpiry.setText("")
                        binding!!.bottomLayout.newCardIncludeLayout.newCardCvv.setText("")
                        binding!!.bottomLayout.savedCardIncludeLayout.savedCardCvv.setText("")

                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else if(data.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else  if (data!!.data!!.code == 6)
                    {
                       payment_id=data!!.data!!.data!!.redirect.paymentId

                       var intent = Intent(requireActivity(), PaymentWebViewActivity::class.java)
                       intent.putExtra("loadUrl", data!!.data!!.data!!.redirect.redirectUrl)
                       startActivityForResult(intent, GlobalConstants.RESULT_CODE_SECURE_PAYMENT)
                    }
                    else
                        utilities!!.showAlert("Error", data!!.data!!.data.error)
                }
                Status.ERROR -> {
                 //   viewModel!!.setIsLoading(false, requireActivity())
                    progressbar!!.hide()
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun createLoyalityRequestBody( billing_address: String,
                                           billing_city: String,
                                           billing_county: String,
                                           billing_postal_code: String): LoyalityRequestBody {
        var requestBody = LoyalityRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")

        var cvv = ""
        if (carId == 0) {
            cvv = binding!!.bottomLayout.newCardIncludeLayout.newCardCvv!!.text.toString()
            requestBody.card_expiry =
                binding!!.bottomLayout.newCardIncludeLayout.newCardExpiry!!.text.toString()
                    .replace("/", "")
            requestBody.card_holder =
                binding!!.bottomLayout.newCardIncludeLayout.newCardName!!.text.toString()
            requestBody.card_no =
                binding!!.bottomLayout.newCardIncludeLayout.newCardNumber!!.text.toString()
                    .replace(" ", "")
            requestBody.is_save_card =
                if (binding!!.bottomLayout.newCardIncludeLayout.newCardCheckBoxButton.isChecked) 1 else 0
        } else
            cvv = binding!!.bottomLayout.savedCardIncludeLayout.savedCardCvv!!.text.toString()
        requestBody.card_id = carId.toString()
        requestBody.cvv = cvv
        requestBody.bill_address = billing_address
        requestBody.bill_city = billing_city
        requestBody.bill_country = billing_county
        requestBody.bill_postalCode = billing_postal_code

        requestBody.amount = progressChangedValue!!.toDouble()

        return requestBody
    }

    private fun callConfirmTopupPayment(paymentStatus:String?) {

        viewModel!!.confirmTopupPayment(confirmTopupPaymentRequestBody(paymentStatus!!))
        viewModel?.setIsLoading(true, requireActivity())

        viewModel!!.confirmTopupPaymentResponse.observe(this, Observer { data ->
            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel!!.setIsLoading(false, requireActivity())

                    if (data.data!!.code == 1) {
                        showTopScuccessAlert(data.data.data.success)
                        utilities?.saveCustomerDetail(data.data.data.customer!!)
                        (activity as MainActivity).hideShowAccountBalance()
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else if(data.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else
                        utilities!!.showAlert("Error", data!!.data!!.data.error)
                }
                Status.ERROR -> {
                    viewModel!!.setIsLoading(false, requireActivity())
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun showTopScuccessAlert(message: String) {

        utilities!!.showAlertWithLayoutId(
            "Success",
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
                    openMainScreen()
                }
            })
    }

    // open Main Activity
    public fun openMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


    private fun confirmTopupPaymentRequestBody(paymentStatus:String): ConfirmTopupPaymentRequestBody {
        var requestBody = ConfirmTopupPaymentRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.pid = payment_id
        requestBody.webview_return = paymentStatus
        return requestBody
    }

    private fun selectedPaymentType() {

        if (isAddCardLayoutVisible == 1) {
            binding!!.bottomLayout.hideShowCardLayout.visibility = View.VISIBLE
            binding!!.bottomLayout.imgHideShowCardButton.setRotation(0F)
        } else {
            binding!!.bottomLayout.hideShowCardLayout.visibility = View.GONE
            binding!!.bottomLayout.imgHideShowCardButton.setRotation(180F)
        }

        if (isSavedCardSelected == 1) {
            binding!!.bottomLayout.savedCardsBtn.setBackgroundResource(R.drawable.round_rectangular_pantone_light)
            binding!!.bottomLayout.newCardBtn.setBackgroundResource(R.drawable.round_rectangular_sea_dark)
            binding!!.bottomLayout.newCardIncludeLayout.newCardLayout.visibility = View.GONE
            binding!!.bottomLayout.savedCardIncludeLayout.savedCardLayout.visibility = View.VISIBLE
        } else {
            binding!!.bottomLayout.savedCardsBtn.setBackgroundResource(R.drawable.round_rectangular_sea_dark)
            binding!!.bottomLayout.newCardBtn.setBackgroundResource(R.drawable.round_rectangular_pantone_light)
            binding!!.bottomLayout.newCardIncludeLayout.newCardLayout.visibility = View.VISIBLE
            binding!!.bottomLayout.savedCardIncludeLayout.savedCardLayout.visibility = View.GONE
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.savedCardsBtn -> {
                if (savedCardsArraylist!!.size == 0) {
                    utilities!!.showAlert("Card", "No Saved Card available yet")
                } else if (savedCardsArraylist!!.size > 0) {
                    isSavedCardSelected = 1
                    setSavedCardsAdapter()
                    selectedPaymentType()
                }
            }

            R.id.newCardBtn -> {
                isSavedCardSelected = 0
                selectedPaymentType()
            }
            R.id.bt_newCardPayNow -> {
                carId = 0
                var validation = Validation(requireContext(), utilities!!)

                if (validation.cardValidate(
                        binding!!.bottomLayout.newCardIncludeLayout!!.newCardName,
                        binding!!.bottomLayout.newCardIncludeLayout!!.newCardNumber,
                        binding!!.bottomLayout.newCardIncludeLayout!!.newCardExpiry,
                        binding!!.bottomLayout.newCardIncludeLayout!!.newCardCvv
                    )
                ) {
                    if(progressChangedValue != 0)  {

                        hideShowBottomSheet()
                        hideShowSelectedCardLayout()

                        billingdAddressDialog=BillingdAddressDialog(context,this)

                        billingdAddressDialog!!.showAddressPopup(
                          "",
                            "",
                            "",
                            "",
                            "NewCard"
                        )

                        utilities!!.hideKeyboard(
                            binding!!.bottomLayout.newCardIncludeLayout.btNewCardPayNow,
                            requireActivity()
                        )
                    }
                    else{
                        utilities!!.showAlert("Amount", "Please Add amount in top up ")
                    }
                }
            }
            R.id.bt_savedCardPayNow -> {
                binding!!.bottomLayout.newCardIncludeLayout!!.newCardName.setText("")
                binding!!.bottomLayout.newCardIncludeLayout!!.newCardNumber.setText("")
                binding!!.bottomLayout.newCardIncludeLayout!!.newCardExpiry.setText("")
                binding!!.bottomLayout.newCardIncludeLayout!!.newCardExpiry.setText("")

                carId = savedCardsArraylist!!.get(selectedSavedCardPos).card_id.toInt()

                if (binding!!.bottomLayout.savedCardIncludeLayout.savedCardCvv.text.toString()
                        .isNotEmpty()
                ) {
                    if (progressChangedValue != 0) {
                        hideShowSelectedCardLayout()
                        hideShowBottomSheet()

                       // callloyaltyApi()
                        billingdAddressDialog=BillingdAddressDialog(context,this)
                        billingdAddressDialog!!.showAddressPopup(
                            savedCardsArraylist!!.get(selectedSavedCardPos).billing.address,
                            savedCardsArraylist!!.get(selectedSavedCardPos).billing.city,
                            savedCardsArraylist!!.get(selectedSavedCardPos).billing.country,
                            savedCardsArraylist!!.get(selectedSavedCardPos).billing.postalcode
                            ,"SavedCard"
                        )

                        utilities!!.hideKeyboard(
                            binding!!.bottomLayout.savedCardIncludeLayout.btSavedCardPayNow,
                            requireActivity()
                        )
                    }
                    else{
                        utilities!!.showAlert("Amount", "Please Add amount in top up ")
                    }
                }
                else {
                    utilities!!.showAlert("Card", "Please Enter the CVV No")
                }
            }
            R.id.scanCard -> {
                scanCreditCard()
            }
        }
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

    private fun getSavedCards() {
        viewModel!!.getSavedCards(getSavedCardsRequestBody())
        viewModel!!.setIsLoading(true, requireActivity())

        viewModel!!.getSavedCardResponse.observe(requireActivity(), Observer { data ->
            viewModel!!.setIsLoading(false, requireActivity())

            when (data!!.status) {
                Status.SUCCESS -> {
                    if (data.data!!.code == 1) {
                        savedCardsArraylist =
                            data.data!!.data.savedCards as java.util.ArrayList<SavedCards>
                        binding!!.qrCodeScanLayout.visibility=View.VISIBLE
                        setupLoyalty(data.data!!.data.loyalty as Loyalty)

                        if(data.data!!.data.loyalty_card.card_mask!=null)
                        {
                            var qrCode= getQRCode(500, 500, data.data!!.data.loyalty_card.card_mask)
                            if (qrCode != null) {
                                binding!!.imgQr.setImageBitmap(qrCode)

                                binding!!.loyaltyCardNoLayout.visibility=View.VISIBLE
                                binding!!.loyaltyCardNo.setText(data.data!!.data.loyalty_card.card_mask)
                                binding!!.noCarLinked.visibility=View.GONE
                                binding!!.imgQr.visibility=View.VISIBLE
                                binding!!.btnMyCode.visibility=View.VISIBLE
                                binding!!.btnScanCode.visibility=View.VISIBLE
                          }
                            else
                            {
                                binding!!.noCarLinked.visibility=View.VISIBLE
                                binding!!.imgQr.visibility=View.GONE
                            }
                        }
                        else
                        {
                            binding!!.noCarLinked.visibility=View.VISIBLE
                            binding!!.imgQr.visibility=View.GONE
                        }
                   }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else if(data.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else
                        utilities!!.showAlert("Error", data!!.data!!.data.error)
                }
                Status.ERROR -> {
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun setupLoyalty(loyalty: Loyalty) {

        var layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.recylerviewLoyalty!!.setLayoutManager(layoutManager)

        var cupAdapter = CupAdapter((activity as MainActivity).ComputeLoyalty(loyalty), loyalty)
        binding!!.recylerviewLoyalty!!.setAdapter(cupAdapter)
    }


    private fun getSavedCardsRequestBody(): GetSavedCardRequestBody {
        var requestBody = GetSavedCardRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.onlycards =1
        return requestBody
    }

    private fun addLoyaltyCardApi(qrCode:String) {
        viewModel!!.addloyaltyCard(addLoyaltyCard(qrCode))
        viewModel!!.setIsLoading(true, requireActivity())

        viewModel!!.addLoyaltyCardResponse.observe(requireActivity(), Observer { data ->
            viewModel!!.setIsLoading(false, requireActivity())

            when (data!!.status) {
                Status.SUCCESS -> {
                    if (data.data!!.code == 1) {
                        utilities!!.showAlert("Success", data!!.data!!.data.success)
                        getSavedCards()
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else if(data.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else
                       utilities!!.showAlert("Error", data!!.data!!.data.error)
                }
                Status.ERROR -> {
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun addLoyaltyCard(qrCode:String): AddLoyalityCardRequestBody
    {
        var requestBody = AddLoyalityCardRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.card_no =qrCode
        return requestBody
    }

    private fun setSavedCardsAdapter() {
        var layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.bottomLayout.savedCardIncludeLayout!!.recyclerSavedCard.setLayoutManager(
            layoutManager
        )
        savedCardsArraylist!!.get(selectedSavedCardPos).isSelected = 1
        // category adpter
        var savedCardAdapter =
            SavedCardAdapter(
                requireActivity(),
                savedCardsArraylist!!,
                this
            )
        binding!!.bottomLayout.savedCardIncludeLayout!!.recyclerSavedCard!!.setAdapter(
            savedCardAdapter
        )
    }

    override fun onSavedCardSelected(pos: Int) {
        selectedSavedCardPos = pos
    }

    fun hideShowBottomSheet() {
        if (sheetBehavior!!.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    fun hideShowSelectedCardLayout() {
        if (savedCardsArraylist!!.size > 0) {
            isSavedCardSelected = 1
            setSavedCardsAdapter()
        } else {
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

    /*
 * @method
 * -generate Qr code
 * @return[Bitmap]
 * */
    fun getQRCode(width: Int, height: Int, identityKey: String): Bitmap {
        val writer: QRCodeWriter = QRCodeWriter()
        val bitMatrix = writer.encode(identityKey, BarcodeFormat.QR_CODE, width, height)
        val imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (i in 0 until width) {
            for (j in 0 until height) {
                imageBitmap.setPixel(i, j, if (bitMatrix[i, j]) Color.BLACK else Color.TRANSPARENT)
            }
        }
        return imageBitmap
    }

    override fun addBillingAddress(
        billing_address: String,
        billing_city: String,
        billing_county: String,
        billing_postal_code: String,
        actionType: String
    ) {

        if(Validation(requireContext(),utilities!!).validateBillingAddress(billing_address,billing_city,billing_county,billing_postal_code))
        {
            billingdAddressDialog!!.closeDialog()
            callloyaltyApi(
                billing_address,billing_city,billing_county,billing_postal_code
            )
        }
    }

    fun checkSyncLoyalty() {
        if (utilities!!.getCustomerDetail().syncWithButlers == 1) {
            showLoyaltyLinkOptions()
        } else {
            syncLoyalityPop()
        }
    }

    //show loyalty link options
    fun showLoyaltyLinkOptions() {
        loyaltyLinkOptionDialog = LoyaltyLinkOptionDialog(requireActivity(), this)
        loyaltyLinkOptionDialog!!.showLoyaltyLinkOptions()
    }

    override fun onLinkOptionSelected(linkType: Int) {

        //linkType 0 = manual
        if (linkType == 0) {
            loyaltyCardNumberDialog = LoyaltyCardNumberDialog(requireActivity(), this)
            loyaltyCardNumberDialog!!.showLoyaltyCardNumberDialog()
        }

        //linkType 1 = qr scan
        if (linkType == 1) {
            qrCodeScan()
        }
    }

    override fun onLoyaltyCardNumberSubmitted(loyaltyCardNumbder: String) {
        addLoyaltyCardApi(loyaltyCardNumbder)
    }
}