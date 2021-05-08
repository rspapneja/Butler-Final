package com.butlerschocolates.app.fragment.support

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.support.SupportCommentListAdapter
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentSupportChatBinding
import com.butlerschocolates.app.firebase.MyFirebaseMessagingService
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.global.GlobalConstants.NotifyType
import com.butlerschocolates.app.model.support.add.comment.AddSupportCommentRequestBody
import com.butlerschocolates.app.model.support.detail.Comment
import com.butlerschocolates.app.model.support.detail.SupportTicketDetailRequestBody

import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.viewmodels.support.SupportViewModel


class SupportChatFragment : BaseFragment() {

    var linearLayoutManager: LinearLayoutManager? = null
    var adapter: SupportCommentListAdapter? = null

    val viewModel: SupportViewModel by viewModels()
    var binding: FragmentSupportChatBinding? = null

    var commentList: ArrayList<Comment>? = null

    var isSupportCommentApiCallFirstTime = true
    var oldArrSize = 0
    var b:Boolean=true

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_support_chat, container, false)
        val view: View = binding!!.getRoot()

        viewInitailze()
        binding!!.addComment.setOnClickListener {
            if (binding!!.supportTicketComment.text.toString().trim().length != 0) {
                addSCommentAPI()
            }
            else{
                utilities!!.showAlert("Add Message","Please Enter the message");
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        CallChatListAPi()
    }

    override fun onResume() {
        super.onResume()
        b=true
    }

    private fun CallChatListAPi() {
        if (isAttachedToActivity()||NotifyType==1) {
            if (b) {
                NotifyType == 0
                requireActivity().runOnUiThread(object : Runnable {
                    override fun run() {
                        Handler().postDelayed(object : Runnable {
                            override fun run() {
                                getSupportChatList()
                                CallChatListAPi()
                            }
                        }, 1000)
                    }
                })
            }
        }
}

    private fun viewInitailze() {
        binding!!.viewModel = viewModel
        commentList = ArrayList()
        setupOrderListRecyclerView()
    }

    private fun setupOrderListRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        binding!!.recyclerViewSupportChat.setLayoutManager(linearLayoutManager)

        adapter = SupportCommentListAdapter(requireActivity(), commentList as ArrayList<Comment>)
        binding!!.recyclerViewSupportChat.setAdapter(adapter)
    }

    private fun getSupportChatList() {
        if(isAttachedToActivity()) {
            viewModel.getSupportTicketDetail(
                createSupportChatListRequestBody(),
                requireActivity(),
                isSupportCommentApiCallFirstTime
            )
            viewModel.supportTicketDetailResponse.observe(viewLifecycleOwner, Observer { data ->
                if (isAttachedToActivity()) {
                    when (data!!.status) {

                        Status.SUCCESS -> {
                            viewModel.setIsLoading(false, requireActivity())

                            if (data.data!!.code == 1) {
                              if (isSupportCommentApiCallFirstTime) {

                                binding!!.orderId.setText(data.data!!.data.ticket.customerTicketId.toString())
                                binding!!.ticketCreatedTime.setText(data.data!!.data.ticket.created.toString())

                                if(data.data!!.data.ticket.type.equals("order",true))
                                    binding!!.supportType.setText((data.data!!.data.ticket.type+" "+data.data!!.data.ticket.orderId.toString()).toUpperCase())
                                else
                                      binding!!.supportType.setText(data.data!!.data.ticket.type.toUpperCase())

                                utilities!!.loadImage(data.data!!.data.ticket.store_logo,binding!!.storeLogo)
                                isSupportCommentApiCallFirstTime = false
                            }

                                binding!!.ticketStatus.setText(data.data!!.data.ticket.status.toUpperCase())

                                commentList!!.clear()
                                commentList!!.addAll(data.data!!.data.ticket.comments)

                                if (oldArrSize != commentList!!.size) {
                                    oldArrSize = commentList!!.size
                                    binding!!.recyclerViewSupportChat.scrollToPosition(commentList!!.size - 1)
                                }
                                adapter!!.notifyDataSetChanged()

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
                            isSupportCommentApiCallFirstTime = false
                            viewModel.setIsLoading(false, requireActivity())
                            utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                        }
                    }
                }
            })
        }
    }

    private fun createSupportChatListRequestBody(): SupportTicketDetailRequestBody {
        var requestBody = SupportTicketDetailRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.ticket_id = GlobalConstants.SUPPORT_TICKET_ID
        return requestBody
    }

    private fun addSCommentAPI() {
        viewModel.addSupportComment(CreateAddSupportCommentRequestBody(), requireActivity())
        viewModel.addSupportCommentResponse.observe(viewLifecycleOwner, Observer { data ->

            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel.setIsLoading(false, requireActivity())

                    if (data.data!!.code == 1) {
                        binding!!.supportTicketComment.setText("")
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
                    viewModel.setIsLoading(false, requireActivity())
                    utilities!!.showAlert("Error", utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun CreateAddSupportCommentRequestBody(): AddSupportCommentRequestBody {
        var requestBody = AddSupportCommentRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.ticket_id = GlobalConstants.SUPPORT_TICKET_ID
        requestBody.message = binding!!.supportTicketComment.text.toString()
        return requestBody
    }

    override fun onPause() {
        super.onPause()
        b=false
    }
}