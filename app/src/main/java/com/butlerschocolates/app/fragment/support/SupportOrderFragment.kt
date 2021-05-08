package com.butlerschocolates.app.fragment.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.myorderlist.MyOrdersListAdapter
import com.butlerschocolates.app.adapter.support.SupportOrdersListAdapter
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentSupportOrderBinding
import com.butlerschocolates.app.dialog.SubmitQueryDialog
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.orderlist.OrderListRequestBody
import com.butlerschocolates.app.model.orderlist.Succes
import com.butlerschocolates.app.model.support.add.ticket.AddSupportTicketRequestBody
import com.butlerschocolates.app.model.support.get.orderlist.Order
import com.butlerschocolates.app.model.support.get.orderlist.SupportOrderListRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.viewmodels.support.SupportViewModel


class SupportOrderFragment : BaseFragment(), SupportOrdersListAdapter.SupportOrderClickedListener,
    SubmitQueryDialog.SubmitQuery {

    var linearLayoutManager: LinearLayoutManager? = null
    var supportOrdersListAdapter: SupportOrdersListAdapter? = null

    val viewModel: SupportViewModel by viewModels()
    var binding: FragmentSupportOrderBinding? = null

    var submitQueryDialog: SubmitQueryDialog? = null

    var order: ArrayList<Order>? = null

    var PAGE_NO = 1
    var loading = true // True if we are still waiting for the last set of data to load.

    var ticketSupportType = ""
    var orderId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_support_order, container, false)
        val view: View = binding!!.getRoot()

        viewInitailze()
        getSupportOrderList()

        binding!!.linearGeneralChat.setOnClickListener {
            ticketSupportType = "general"
            submitQueryDialog!!.showSupportDialog()
        }

        return view
    }

    override fun onSupportOrderItemSelected(order: Order, pos: Int) {
        orderId = order.orderId
        ticketSupportType = "order"
        submitQueryDialog!!.showSupportDialog()
    }

    override fun onSubmitQuery(query: String) {
        addSupportTicketAPI(query)
    }

    private fun viewInitailze() {
        submitQueryDialog = SubmitQueryDialog(activity, this)
        binding!!.viewModel = viewModel

        order = ArrayList()

        setupOrderListRecyclerView()
        onScrollListener()
    }

    private fun setupOrderListRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        binding!!.supportOrderList.setLayoutManager(linearLayoutManager)

        supportOrdersListAdapter = SupportOrdersListAdapter(requireActivity(), order!!, this)
        binding!!.supportOrderList.setAdapter(supportOrdersListAdapter)
    }

    private fun getSupportOrderList() {
        viewModel.getSupportOrderList(createSupportOrderListRequestBody(), requireActivity())

        viewModel.supportOrderListResponse.observe(viewLifecycleOwner, Observer { data ->

            when (data!!.status) {
                Status.SUCCESS -> {

                    viewModel.setIsLoading(false, requireActivity())

                    if (data.data!!.code == 1) {
                        order!!.addAll(data.data!!.data.orders)
                        supportOrdersListAdapter!!.notifyDataSetChanged()
                        loading = data!!.data!!.data.isNext


                        if (order!!.size == 0 && PAGE_NO == 1) {

                            hideShowNoRecordFoundLayout(
                                getString(R.string.no_order_message),
                                binding!!.supportOrderList,
                                binding!!.noFound.imgNoImageFound,
                                binding!!.noFound!!.tvNoTextFound,
                              binding!!.noFound.noFoundLayout,
                                R.drawable.ic_no_data_found, 0
                            )
                        } else {
                            hideShowNoRecordFoundLayout(
                                getString(R.string.no_order_message),
                                binding!!.supportOrderList,
                                binding!!.noFound.imgNoImageFound,
                                binding!!.noFound!!.tvNoTextFound,
                                binding!!.noFound.noFoundLayout,
                                R.drawable.ic_no_data_found, 1
                            )
                        }

                        if (loading)
                            PAGE_NO++

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

    private fun createSupportOrderListRequestBody(): SupportOrderListRequestBody {
        var requestBody = SupportOrderListRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.page = PAGE_NO
        return requestBody
    }

    /***************************************loadMoreData ****************************************/
    fun onScrollListener() {
        binding!!.supportOrderList!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);

                var visibleItemCount = recyclerView!!.childCount
                var totalItemCount = linearLayoutManager!!.itemCount
                var firstVisibleItem = linearLayoutManager!!.findFirstVisibleItemPosition()

                if (loading && firstVisibleItem + visibleItemCount >= totalItemCount) {
                    getSupportOrderList()
                    loading = false
                }
            }
        })
    }

    private fun addSupportTicketAPI(message: String) {
        viewModel.addSupportTicket(CreateAddSupportTicketRequestBody(message), requireActivity())
        viewModel.addSupportTicketResponse.observe(viewLifecycleOwner, Observer { data ->

            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel.setIsLoading(false, requireActivity())

                    if (data.data!!.code == 1) {
                        utilities!!.showAlert("Success", data!!.data!!.data.message)

                        (activity as MainActivity).onBackPressed()
                    }
                    else if (data.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(data.data!!.data.error)
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

    private fun CreateAddSupportTicketRequestBody(message: String): AddSupportTicketRequestBody {
        var requestBody = AddSupportTicketRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.type = ticketSupportType
        requestBody.message = message
        if (ticketSupportType.equals("order")) requestBody.order_id = orderId
        return requestBody
    }
}
