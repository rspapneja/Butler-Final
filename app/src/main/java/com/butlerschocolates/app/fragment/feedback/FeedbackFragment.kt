package com.butlerschocolates.app.fragment.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.databinding.FragmentFeedbackBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.feedback.FeedbackRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.feedback.FeedbackViewModel


class FeedbackFragment : Fragment() {

    var binding: FragmentFeedbackBinding?=null
    var utilities: Utilities?=null
    var feedbackViewModel:FeedbackViewModel?=null

    var selectedContent: String? = null
    var selectedEasyTouse: String? = null
    var selectedDesign: String? = null
    var selectedOverall: String? = null

    var selectedFeelingToday: String? = null
    var errorMessage = ""

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feedback, container, false)
        val view: View = binding!!.getRoot()
        
        initView()
        setupViewModel()

        binding!!.submitFeedback.setOnClickListener {
        if(validateForm())
             callFeedbackApi()
        else
            utilities!!.showAlert("Feedback",errorMessage)
        }
        return view
    }

    private fun setupViewModel() {
        feedbackViewModel = ViewModelProviders.of(this).get(FeedbackViewModel::class.java)
        feedbackViewModel!!.init()

        binding!!.viewModel=feedbackViewModel
    }

    private fun initView() {
        utilities = Utilities(requireActivity())
    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<TextView>(R.id.txt_title).setText(GlobalConstants.ScreenTitle)
    }

    // call faq list api
    private fun callFeedbackApi() {
               feedbackViewModel!!.sendFeedbackRequest(createFeedbackRequestBody(),requireActivity())!!.observe(
                requireActivity(), androidx.lifecycle.Observer {

                  if (it!!.getData() != null) {
                       // call is successful
                      if(it!!.getData().code==1) {
                          binding!!.comment!!.text.clear()
                          binding!!.content!!.clearCheck()
                          binding!!.easyToUse!!.clearCheck()
                          binding!!.design!!.clearCheck()
                          binding!!.overall!!.clearCheck()
                          binding!!.emojiRadio.clearCheck()
                          utilities!!.showAlert("Success", it!!.getData().data.success)
                      }
                      else if(it!!.getData().code == 4){
                          (activity as MainActivity).handleAuthTokenAlert(it!!.getData().data.error)
                      }
                      else if(it!!.getData().code==301) {
                          (activity as MainActivity).redirectNotificationFragement()
                      }
                    } else {
                        // call failed.
                        utilities!!.showAlert("Error",utilities!!.apiAlert(it.error).toString())
                    }
                       feedbackViewModel!!.setIsLoading(false,requireActivity())
                   }
             )
    }

    private fun createFeedbackRequestBody(): FeedbackRequestBody {
        var faqRequestBady = FeedbackRequestBody()
        faqRequestBady.version = AppConstants.API_VERSION
        faqRequestBady.auth_token =  utilities!!.readPref("Auth_Token")
        faqRequestBady.content =  selectedContent
        faqRequestBady.easy_to_use =  selectedEasyTouse
        faqRequestBady.design =   selectedDesign
        faqRequestBady.overall = selectedOverall
        faqRequestBady.feeling_today = selectedFeelingToday
        faqRequestBady.comment = binding!!.comment.text.toString()
        return faqRequestBady
    }

     fun validateForm():Boolean {
         errorMessage=""

        var validContent = false
        var valideasyToUse = false
        var validDesign = false
        var validOverall = false

        if (binding!!.contentAwesome!!.isChecked() || binding!!.contentBest!!.isChecked() || binding!!.contentPoor!!.isChecked() || binding!!.contentNeutral!!.isChecked()) {
            if (binding!!.contentAwesome!!.isChecked())
                selectedContent = "Excellent"
            if (binding!!.contentBest!!.isChecked())
                selectedContent = "Good"
            if (binding!!.contentPoor!!.isChecked())
                selectedContent = "Poor"
            if (binding!!.contentNeutral!!.isChecked())
                selectedContent = "Okay"
            validContent = true
        } else {
            validContent = false
            errorMessage += "Select at least one Content\n"
        }

        if (binding!!.easyToUseAwesome!!.isChecked() || binding!!.easyToUseBest!!.isChecked() || binding!!.easyToUsePoor!!.isChecked() || binding!!.easyToUseNeutral!!.isChecked()) {
            if (binding!!.easyToUseAwesome!!.isChecked())
                selectedEasyTouse = "Excellent"
            if (binding!!.easyToUseBest!!.isChecked())
                selectedEasyTouse = "Good"
            if (binding!!.easyToUsePoor!!.isChecked())
                selectedEasyTouse = "Poor"
            if (binding!!.easyToUseNeutral!!.isChecked())
                selectedEasyTouse = "Okay"
            valideasyToUse = true
        } else {
            valideasyToUse = false
            errorMessage += "Select at least one Easy to use\n"
        }

        if (binding!!.designAwesome!!.isChecked() || binding!!.designBest!!.isChecked() ||binding!!.designOkay!!.isChecked() || binding!!.designPoor!!.isChecked()) {
            if (binding!!.designAwesome!!.isChecked())
                selectedDesign = "Excellent"
            if (binding!!.designBest!!.isChecked())
                selectedDesign = "Good"
            if (binding!!.designPoor!!.isChecked())
                selectedDesign = "Poor"
            if (binding!!.designOkay!!.isChecked())
                selectedDesign = "Okay"
            validDesign = true
        } else {
            validDesign = false
            errorMessage += "Select at least one Design\n"
        }

        if (binding!!.overallAwesome!!.isChecked() || binding!!.overallGood!!.isChecked() || binding!!.overallOkay!!.isChecked() || binding!!.overallPoor!!.isChecked()) {
            if (binding!!.overallAwesome!!.isChecked())
                selectedOverall = "Excellent"
            if (binding!!.overallGood!!.isChecked())
                selectedOverall = "Good"
            if (binding!!.overallPoor!!.isChecked())
                selectedOverall = "Poor"
            if (binding!!.overallOkay!!.isChecked())
                selectedOverall = "Okay"
            validOverall = true
        } else {
            validOverall = false
            errorMessage += "Select at least one Overall.\n"
        }

        if (binding!!.emojiAwesome!!.isChecked || binding!!.emojiSmile!!.isChecked || binding!!.emojiConfused!!.isChecked ||binding!!.emojiBad!!.isChecked) {
            if(binding!!.emojiAwesome!!.isChecked())
                selectedFeelingToday = "Excellent"
            if (binding!!.emojiConfused!!.isChecked())
                selectedFeelingToday = "Good"
            if (binding!!.emojiSmile!!.isChecked())
                selectedFeelingToday = "Good"
            if (binding!!.emojiBad!!.isChecked())
                selectedFeelingToday = "Okay"
            valideasyToUse = true
        } else {
            valideasyToUse = false
            errorMessage += "Select how are you feeling today\n"
        }

        if (validContent && validDesign && valideasyToUse && validOverall) {
            return true
        } else {
         return false
        }
    }
}
