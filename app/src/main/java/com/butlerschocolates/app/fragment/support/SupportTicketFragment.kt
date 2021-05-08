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
import com.butlerschocolates.app.adapter.support.SupportTicketListAdapter
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentSupportBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.support.get.ticket.list.SupportTicketListRequestBody
import com.butlerschocolates.app.model.support.get.ticket.list.Ticket
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.viewmodels.support.SupportViewModel


class SupportTicketFragment : BaseFragment(),
    SupportTicketListAdapter.SupportTicketClickedListener {

    var linearLayoutManager: LinearLayoutManager? = null
    var supportListAdapter: SupportTicketListAdapter? = null

    var binding: FragmentSupportBinding? = null
    val viewModel: SupportViewModel by viewModels()

    var ticketList: ArrayList<Ticket>? = null

    var PAGE_NO = 1
    var loading = true // True if we are still waiting for the last set of data to load.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false)
        val view: View = binding!!.getRoot()

        binding!!.addTicketLayout.setOnClickListener {

            Navigation.findNavController(requireActivity(), R.id.nav_host_container)
                .navigate(R.id.supportOrderFragment, null)
        }

        viewInitaize(view)
        getSupportOrderList()
        return view
    }

    private fun viewInitaize(v: View?) {
        binding!!.viewModel = viewModel
        ticketList = ArrayList()

        setupOrderListRecyclerView()
        onScrollListener()
    }

    private fun setupOrderListRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        binding!!.recyclerViewSupportList.setLayoutManager(linearLayoutManager)

        supportListAdapter = SupportTicketListAdapter(requireActivity(), ticketList!!, this)
        binding!!.recyclerViewSupportList.setAdapter(supportListAdapter)
    }

    private fun getSupportOrderList() {
        viewModel.getSupportTicketList(createSupportTicketListRequestBody(), requireActivity())
        viewModel.supportTicketListResponse.observe(viewLifecycleOwner, Observer { data ->

            when (data!!.status) {
                Status.SUCCESS -> {
                    viewModel.setIsLoading(false, requireActivity())

                    if (data.data!!.code == 1) {
                        ticketList!!.addAll(data.data!!.data.tickets)
                        supportListAdapter!!.notifyDataSetChanged()
                        loading = data!!.data!!.data.isNext

                        if (ticketList!!.size == 0 && PAGE_NO == 1) {

                            hideShowNoRecordFoundLayout(
                                getString(R.string.no_ticket_message),
                                binding!!.recyclerViewSupportList,
                                binding!!.noFound.imgNoImageFound,
                                binding!!.noFound!!.tvNoTextFound,
                                binding!!.noFound.noFoundLayout,
                                R.drawable.ic_no_ticket, 0
                            )
                        } else {
                            hideShowNoRecordFoundLayout(
                                getString(R.string.no_ticket_message),
                                binding!!.recyclerViewSupportList,
                                binding!!.noFound.imgNoImageFound,
                                binding!!.noFound!!.tvNoTextFound,
                                binding!!.noFound.noFoundLayout,
                                R.drawable.ic_no_ticket, 1
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

    private fun createSupportTicketListRequestBody(): SupportTicketListRequestBody {
        var requestBody = SupportTicketListRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.page = PAGE_NO
        return requestBody
    }

    /***************************************loadMoreData ****************************************/
    fun onScrollListener() {
        binding!!.recyclerViewSupportList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
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

    override fun onSupportTicketSelected(ticket: Ticket, pos: Int) {
        GlobalConstants.SUPPORT_TICKET_ID = ticket.ticketId
        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.supportChatFragment, null)
    }
}
