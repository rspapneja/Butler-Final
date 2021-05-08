package com.butlerschocolates.app.fragment.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.BootStrapProcessActivity
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.getSavedCard.SavedCardsAdapter
import com.butlerschocolates.app.api.ApiClient
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.callback.ImagePickerCallBack
import com.butlerschocolates.app.databinding.FragmentAccountBinding
import com.butlerschocolates.app.dialog.SavedCardDialog
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Customer
import com.butlerschocolates.app.model.deleteSavedCard.RemoveSavedCardRequestBody
import com.butlerschocolates.app.model.getSavedCard.GetSavedCardRequestBody
import com.butlerschocolates.app.model.logout.LogoutRequestBody
import com.butlerschocolates.app.model.storetiming.SavedCards
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.util.Validation
import com.butlerschocolates.app.viewmodels.updateProfile.UpdateProfileViewModel

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList

class AccountFragment : BaseFragment(), View.OnClickListener, ImagePickerCallBack,SavedCardDialog.SavedCardListener {

    var binding: FragmentAccountBinding? = null
    var validation: Validation? = null

    var userImageFilePath: String? = null
    var viewModel: UpdateProfileViewModel? = null
    var customer: Customer? = null

    var savedCardsArraylist:ArrayList<SavedCards>?=null
    var savedCardsAdapter:SavedCardsAdapter?=null
    var savedCardDialog:SavedCardDialog?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment by data binding
        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        val view: View = binding!!.getRoot()

        viewInitailize()

        setBannerData()

        checkLoginStatus()

        setupListerner()

        setupViewModel()

        return view
    }

    //  hide Show Edit Profile Icon
    private fun hideShowEditProfileIcon(b: Boolean) {
        binding!!.imgEditProfile.visibility = if (b) View.VISIBLE else View.GONE
        binding!!.tvSaveProfile.visibility = if (b) View.GONE else View.VISIBLE
    }

    //  hide Show Edit user fields
    private fun enableDisableUserField(b: Boolean) {
        binding!!.edName.isEnabled = b
        binding!!.edtxtPhone.isEnabled = b
        binding!!.countryCodePicker.setCcpClickable(b)
        binding!!.imgUserPic.isClickable = if (b) false else true
    }

    // set the user Detail
    private fun setUserInfoData() {
        customer = utilities!!.getCustomerDetail()
        utilities!!.loadImage(customer!!.profilePic, binding!!.imgUserPic)

        binding!!.edName.setText(customer!!.customerName)
        binding!!.edEmail.setText(customer!!.email)
        binding!!.edtxtPhone.setText(customer!!.phone)
        binding!!.countryCodePicker.setCountryForPhoneCode(customer!!.countryCode.toInt())
    }

    // set banner image
    private fun setBannerData() {
        utilities!!.loadImage(utilities!!.getHomeApiData().profile_banner, binding!!.imgBanner)
        binding!!.appVersion.text = utilities!!.getAppVerion()
    }

    private fun viewInitailize() {
        utilities = Utilities(requireActivity())
        validation = Validation(requireActivity(), utilities!!)

        savedCardDialog = SavedCardDialog(activity, this)
        binding!!.viewModel=viewModel
    }

    //  hide and shows view according to login status
    private fun checkLoginStatus() {
        if (utilities!!.readPref("isLogin").equals("true")) {
            binding!!.layoutUserDetail.visibility = View.VISIBLE
            binding!!.tvGuest.visibility = View.GONE
            binding!!.txtLoginLogout.setText("Logout")

            enableDisableUserField(false)
            hideShowEditProfileIcon(true)
            setUserInfoData()
        } else {
            binding!!.layoutUserDetail.visibility = View.GONE
            binding!!.tvGuest.visibility = View.VISIBLE
            binding!!.imgEditProfile.visibility = View.GONE
            binding!!.txtLoginLogout.setText("Log in")
        }
    }

    private fun setupListerner() {
        binding!!.rlContactus?.setOnClickListener(this)
        binding!!.rlFaq?.setOnClickListener(this)
        binding!!.rlMyOrder?.setOnClickListener(this)
        binding!!.rlFeedback.setOnClickListener(this)
        binding!!.rlSetting?.setOnClickListener(this)
        binding!!.rlPrivacyPolicy?.setOnClickListener(this)
        binding!!.rlSupport?.setOnClickListener(this)
        binding!!.rlTerms?.setOnClickListener(this)

        binding!!.tvSaveProfile?.setOnClickListener(this)
        binding!!.imgEditProfile?.setOnClickListener(this)
        binding!!.layoutUserImage?.setOnClickListener(this)
        binding!!.txtLoginLogout?.setOnClickListener(this)
        binding!!.txtCards?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.rl_contactus -> {
                GlobalConstants.ScreenTitle = "Contact us"
                Navigation.findNavController(p0!!).navigate(R.id.contactUSFragment, null)
            }
            R.id.rl_my_order -> {
                if (!utilities!!.readPref("isLogin").equals("true")) {
                    utilities!!.showAlert("Login", "Please Login First.")
                } else {
                    Navigation.findNavController(p0!!).navigate(R.id.myOrderListFragment, null)
                }
            }
            R.id.txt_LoginLogout -> {
                if (utilities!!.readPref("isLogin").equals("true")) {
                    utilities!!.showAlertWithActions(
                        "Confirmation", "Are you sure you want to logout?", "YES", "CLOSE",
                        object : Utilities.ActionButtons {
                            override fun okAction() {
                                viewModel!!.logoutInitalize()
                                logoutApi()
                            }
                            override fun cancelAction() {}
                        })
                } else {
                    startActivity(
                        Intent(
                            activity,
                            BootStrapProcessActivity::class.java
                        ).putExtra("logout", "logout")
                    )
                    requireActivity().finish()
                }
            }
            R.id.layout_UserImage -> {
                (activity as MainActivity).requestCameraAndGalleryPermissions(
                    requireActivity(),
                    this
                );
            }

            R.id.rl_setting -> {
                GlobalConstants.ScreenTitle = "Settings"
                if (utilities!!.readPref("isLogin").equals("true")) {
                    Navigation.findNavController(p0!!).navigate(R.id.settingsFragment, null)
                } else {
                    utilities!!.showAlert("Login", "Please Login First.")
                }
            }

            R.id.rl_privacyPolicy -> {
                GlobalConstants.URL = utilities!!.getHomeApiData().privacy_policy
                GlobalConstants.ScreenTitle = "Privacy policy"
                Navigation.findNavController(p0!!).navigate(R.id.webViewfragment, null)
            }

            R.id.rl_terms -> {
                GlobalConstants.URL = utilities!!.getHomeApiData().terms_conditions
                GlobalConstants.ScreenTitle = " Terms & Conditions"

                Navigation.findNavController(p0!!).navigate(R.id.webViewfragment, null)
            }

            R.id.tv_saveProfile -> {
                enableDisableUserField(false)
                hideShowEditProfileIcon(true)

                if (userImageFilePath != null) UpdateProfileApiWithMultipart() else callupdateApi()
            }

            R.id.img_editProfile -> {
                enableDisableUserField(true)
                hideShowEditProfileIcon(false)
            }

            R.id.rl_support -> {
                if (utilities!!.readPref("isLogin").equals("true"))
                    Navigation.findNavController(p0!!).navigate(R.id.supportFragment, null)
                else
                    utilities!!.showAlert("Login", "Please Login First.")
            }

            R.id.rl_faq -> {
                GlobalConstants.ScreenTitle = "FAQs"
                Navigation.findNavController(p0!!).navigate(R.id.faqFragment, null)
            }

            R.id.rl_feedback -> {
                GlobalConstants.ScreenTitle = "Feedback"
                if (utilities!!.readPref("isLogin").equals("true"))
                    Navigation.findNavController(p0!!).navigate(R.id.feedbackFragment, null)
                else
                    utilities!!.showAlert("Login", "Please Login First.")
            }
            R.id.txt_Cards -> {
                if (utilities!!.readPref("isLogin").equals("true"))
                    getSavedCards()
                else
                    utilities!!.showAlert("Login", "Please Login First.")
            }
        }
    }

    private fun logoutApi() {
     viewModel!!.logoutRequest(createLogutRequestBody(), requireActivity())!!.observe(
            requireActivity(), androidx.lifecycle.Observer {
                // hide progress loading
                viewModel!!.setIsLoading(false, requireActivity())

                if (it!!.getData() != null) {

                    // api call is successful
                    utilities!!.showAlertWithLayoutId(
                        "Logout",
                        R.layout.pop_validation_alert,
                        it.getData().data.success,
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
                                openMainScreen()
                            }
                        })

                } else {
                    // api call failed.
                    utilities!!.showAlert("Error", utilities!!.apiAlert(it.error))
                }
            }
        )
    }

    // return update profile map without multiparts
    private fun updateProfileMap(): java.util.HashMap<String, RequestBody> {

        val map = java.util.HashMap<String, RequestBody>()
        map["version"] = ApiClient.createRequestBody(AppConstants.API_VERSION)
        map["auth_token"] =
            ApiClient.createRequestBody(Utilities(requireActivity()).readPref("Auth_Token"))
        map["name"] = ApiClient.createRequestBody(binding!!.edName.text.toString())
        map["country_code"] =
            ApiClient.createRequestBody(binding!!.countryCodePicker.selectedCountryCode)
        map["mobile"] = ApiClient.createRequestBody(binding!!.edtxtPhone.text.toString())
        return map
    }

    // return update profile map with multiparts
    fun createProfileUpdateMultiPartImage(userImageFilePath: String): Array<MultipartBody.Part?> {
        val partsArr = arrayOfNulls<MultipartBody.Part>(1)
        if (userImageFilePath != null) {
            val body = RequestBody.create("image/jpeg".toMediaType(), File(userImageFilePath))
            partsArr[0] =
                MultipartBody.Part.createFormData("profilepic", File(userImageFilePath).name, body)
        }
        return partsArr
    }

    override fun showUserImagePath(UserImageFile: String) {
        Log.e("userImageFile", UserImageFile)
        userImageFilePath = UserImageFile
        utilities!!.loadImageWithFilePath(UserImageFile, binding!!.imgUserPic)
    }

    // setup the view model
    private fun setupViewModel() {

        viewModel = ViewModelProviders.of(this).get(UpdateProfileViewModel::class.java)
        viewModel!!.init()

        binding!!.viewModel = viewModel!!
    }

    // call update profile api without update user image
    private fun callupdateApi() {
        viewModel!!.updateProfileRequest(updateProfileMap(), requireActivity())!!.observe(
            requireActivity(), androidx.lifecycle.Observer {

                //  hide progress loading
                viewModel!!.setIsLoading(false, requireActivity())

                if (it!!.getData() != null) {
                    // call is successful
                   if(it!!.getData().code==1) {
                       utilities!!.showAlert("Success", it!!.getData().data.success)
                       utilities!!.saveCustomerDetail(it!!.getData().data.customer);
                       checkLoginStatus()
                   }
                   else if(it!!.getData().code==301) {
                       (activity as MainActivity).redirectNotificationFragement()
                   }
                   else{
                       (activity as MainActivity).handleAuthTokenAlert(it!!.getData().data.error)
                   }
                } else {
                    // call failed.
                    utilities!!.showAlert("Error", utilities!!.apiAlert(it.error))
                }
            }
        )
    }

    /*
    *  call update profile api with update user image
    * */
    private fun UpdateProfileApiWithMultipart() {

        viewModel!!.updateProfileRequest(
            updateProfileMap(),
            createProfileUpdateMultiPartImage(userImageFilePath!!),
            requireActivity()
        )!!.observe(
            requireActivity(), androidx.lifecycle.Observer {

                // hide progressbar
                viewModel!!.setIsLoading(false, requireActivity())

                if (it!!.getData() != null) {
                    if(it!!.getData().code==1) {
                        // call is successful
                        utilities!!.showAlert("Success", it!!.getData().data.success)
                        utilities!!.saveCustomerDetail(it!!.getData().data.customer);
                        // check user is login
                        checkLoginStatus()
                    }
                    else if(it!!.getData().code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else{
                        (activity as MainActivity).handleAuthTokenAlert(it!!.getData().data.error)
                    }
                } else {
                    // call failed.
                    utilities!!.showAlert("Error", utilities!!.apiAlert(it.error))
                }
            }
        )
    }

    // return logout request body
    private fun createLogutRequestBody(): LogoutRequestBody {
        var requestBody = LogoutRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        return requestBody
    }

    // open Main Activity
     fun openMainScreen() {
      val intent = Intent(activity, MainActivity::class.java)
      startActivity(intent)
      requireActivity().finish()
    }

    private fun getSavedCards() {
     viewModel!!.getSavedCards(getSavedCardsRequestBody())
     viewModel!!.setIsLoading(true, requireActivity())

        viewModel!!.getSavedCardResponse.observe(this, Observer { data ->
        viewModel!!.setIsLoading(false, requireActivity())

           when (data!!.status) {
                Status.SUCCESS -> {
                    if (data.data!!.code == 1) {
                        savedCardsArraylist=data.data!!.data.savedCards as ArrayList<SavedCards>

                         savedCardDialog!!.showCardDialog()
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

    private fun getSavedCardsRequestBody(): GetSavedCardRequestBody {
        var requestBody = GetSavedCardRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.onlycards =0
        return requestBody
    }

    private fun removeSaveCard(cardId: Int,pos: Int) {
        viewModel!!.removeSavedCard(removeSavedCard(cardId))
        viewModel!!.setIsLoading(true, requireActivity())

        viewModel!!.removeSavedCardResponse.observe(this, Observer { data ->
           viewModel!!.setIsLoading(false, requireActivity())

            when (data!!.status) {
                Status.SUCCESS -> {

                    if (data.data!!.code == 1) {

                     utilities!!.showAlert("Card", data!!.data!!.data.success)
                     savedCardsArraylist!!.removeAt(pos)
                     savedCardsAdapter!!.notifyDataSetChanged()

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

    private fun removeSavedCard(cardId: Int): RemoveSavedCardRequestBody {
        var requestBody = RemoveSavedCardRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.card_id =cardId.toString()
        return requestBody
    }

    override fun deleteSavedCard(cardId: Int,pos:Int) {

        utilities!!.showAlertWithActions(
            "Card", "Do you want to delete this card?", "YES", "CLOSE",
            object : Utilities.ActionButtons {
                override fun okAction() {
                    removeSaveCard(cardId,pos)
                    savedCardDialog!!.closeDialog()
                }
                override fun cancelAction() {}
            })
    }

    override fun showSavedCardList(savedCardRecyclerView: RecyclerView) {

        var layoutManager = LinearLayoutManager(requireContext())
        savedCardRecyclerView.setLayoutManager(layoutManager)

        savedCardsAdapter =
            SavedCardsAdapter(
                requireContext(),
                savedCardsArraylist!!
                , this
            )
        savedCardRecyclerView.setAdapter(savedCardsAdapter)
    }
}