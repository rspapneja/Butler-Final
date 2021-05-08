package com.butlerschocolates.app.fragment.faq


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.faq.FaqAdapter
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentFaqBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.faq.Faq
import com.butlerschocolates.app.model.faq.FaqRequestBady
import com.butlerschocolates.app.model.query.AddSubmitQueryRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.faq.FaqViewModel
import java.util.*

class FaqFragment : BaseFragment()
{
    var layoutManager: LinearLayoutManager? = null
    var faqViewModel: FaqViewModel? = null

    var faqlist: ArrayList<Faq>? = null
    var faqAdapter: FaqAdapter? = null

    var binding: FragmentFaqBinding?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_faq, container, false)
        val view: View = binding!!.getRoot()

        initView(view)
        setupFaqRecylerView()
        setupViewModel()
        callFaqListApi()

        binding!!.submitFeedback.setOnClickListener {

          if(binding!!.comment.text.toString().trim().length!=0)
             sendQuery()
           else
              utilities!!.showAlert("Enter","Enter your Query")

        }
        return view
    }

    fun initView(view: View) {
        utilities = Utilities(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).findViewById<TextView>(R.id.txt_title).setText("FAQs")
        (activity as MainActivity).findViewById<TextView>(R.id.txt_title).isAllCaps=false
    }

    // setup faq list Recyclerview
    private fun setupFaqRecylerView() {
        faqlist = ArrayList()
        layoutManager = LinearLayoutManager(activity)
        binding!!.recyclerFaq!!.layoutManager = layoutManager
        faqAdapter = FaqAdapter(requireActivity(), faqlist as ArrayList<Faq>)
        binding!!.recyclerFaq!!.adapter = faqAdapter
    }

    // call faq list api
    private fun callFaqListApi() {

            faqViewModel!!.faqApiRequest(createStoreListRequestBody(),requireActivity())!!.observe(
                requireActivity(), androidx.lifecycle.Observer {
                    if (isAttachedToActivity()) {
                        faqViewModel!!.setIsLoading(false, requireActivity())

                        if (it!!.getData() != null) {
                            // call is successful
                            if(it!!.getData().code==1) {
                                binding!!.layoutParent.visibility = View.VISIBLE
                                faqlist!!.addAll(it.getData().data.faqs)
                                faqAdapter!!.notifyDataSetChanged()
                            }
                            else if(it!!.getData().code==301) {
                                (activity as MainActivity).redirectNotificationFragement()
                            }
                        } else {
                            // call failed.
                            utilities!!.showAlert(
                                "Error",
                                utilities!!.apiAlert(it.error).toString()
                            )
                            binding!!.layoutParent.visibility = View.GONE
                        }
                    }
                }
            )
      }

    private fun setupViewModel() {
        faqViewModel = ViewModelProviders.of(this).get(FaqViewModel::class.java)
        faqViewModel!!.init()
        binding!!.viewModel=faqViewModel
    }

    fun createStoreListRequestBody(): FaqRequestBady
    {
        var faqRequestBady = FaqRequestBady()
        faqRequestBady.version = AppConstants.API_VERSION
        return faqRequestBady
    }

    private fun sendQuery() {
        faqViewModel!!.sendQuery(sendQueryRequestBody())
        faqViewModel!!.setIsLoading(true,requireActivity())

        faqViewModel!!.sendQueryResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data->
            faqViewModel!!.setIsLoading(false,requireActivity())

            when (data!!.status) {
                Status.SUCCESS->{
                    if(data!!.data!!.code==1) {
                        utilities!!.showAlert("Success",data!!.data!!.data.success)
                        binding!!.comment.setText("").toString()
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
                    }
                    else
                        utilities!!.showAlert("Error",data!!.data!!.data.error)
                }
                Status.ERROR->{
                    utilities!!.showAlert("Error",utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun sendQueryRequestBody(): AddSubmitQueryRequestBody {
        var requestBody = AddSubmitQueryRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token =  utilities!!.readPref("Auth_Token")
        requestBody.comment =  binding!!.comment.text.toString()
        return requestBody
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).findViewById<TextView>(R.id.txt_title).isAllCaps=true
    }
}