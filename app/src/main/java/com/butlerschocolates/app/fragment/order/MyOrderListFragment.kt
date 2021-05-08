package com.butlerschocolates.app.fragment.order

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
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentMyOrderListBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.orderlist.OrderListRequestBody
import com.butlerschocolates.app.model.orderlist.Succes
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.viewmodels.orderlist.OrderListViewModel


class MyOrderListFragment : BaseFragment(),MyOrdersListAdapter.OrderListClickedListener {

    var linearLayoutManager : LinearLayoutManager?=null
    var myOrdersListAdapter: MyOrdersListAdapter?=null
    val viewModel: OrderListViewModel by viewModels()

    var binding: FragmentMyOrderListBinding? = null

    var list:ArrayList<Succes>?=null

    var PAGE_NO = 1
    var loading = true // True if we are still waiting for the last set of data to load.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order_list, container, false)
        val view: View = binding!!.getRoot()

        binding!!.orderListViewModel=viewModel

        setupOrderListRecyclerView()
        onScrollListener()
        getOrderStatusApi()

        return view
    }

    private fun setupOrderListRecyclerView() {
        list=ArrayList()
        linearLayoutManager =  LinearLayoutManager(activity)
        binding!!.recyclerMyOrderList .setLayoutManager(linearLayoutManager)

        myOrdersListAdapter = MyOrdersListAdapter(requireActivity(),list!!,this)
        binding!!.recyclerMyOrderList.setAdapter(myOrdersListAdapter)
   }


    /***************************************loadMoreData ****************************************/
    fun onScrollListener() {
        binding!!.recyclerMyOrderList!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);

                var visibleItemCount = recyclerView!!.childCount
                var totalItemCount = linearLayoutManager!!.itemCount
                var firstVisibleItem = linearLayoutManager!!.findFirstVisibleItemPosition()

                if (loading && firstVisibleItem + visibleItemCount >= totalItemCount) {
                    getOrderStatusApi()
                    loading = false
                }
            }
        })
    }

    private fun getOrderStatusApi() {
        viewModel!!.getOrderList(createOrderListRequestBody(),requireActivity())
        viewModel!!.orderListResponse.observe(viewLifecycleOwner, Observer { data->

          when (data!!.status) {

                 Status.SUCCESS->{
                   viewModel!!.setIsLoading(false,requireActivity())

                    if(data!!.data!!.code==1) {
                        list!!.addAll(data.data!!.data.success)
                        myOrdersListAdapter!!.notifyDataSetChanged()
                        loading = data!!.data!!.data.isNext


                        if (list!!.size == 0 && PAGE_NO == 1) {

                            hideShowNoRecordFoundLayout(
                                getString(R.string.no_order_message),
                                binding!!.recyclerMyOrderList,
                                binding!!.noFound.imgNoImageFound,
                                binding!!.noFound!!.tvNoTextFound,
                                binding!!.noFound.noFoundLayout,
                                R.drawable.ic_no_data_found, 0
                            )
                        } else {
                            hideShowNoRecordFoundLayout(
                                getString(R.string.no_order_message),
                                binding!!.recyclerMyOrderList,
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
                        utilities!!.showAlert("Error",data!!.data!!.data.error)
                }
                Status.ERROR->{
                    viewModel!!.setIsLoading(false,requireActivity())
                    utilities!!.showAlert("Error",utilities!!.apiAlert(data.throwable!!))
                }
            }
        })
    }

    private fun createOrderListRequestBody(): OrderListRequestBody {
        var requestBody = OrderListRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token =  utilities!!.readPref("Auth_Token")
        requestBody.page =  1
        return requestBody
    }
    override fun onOrderItemSelected(order: Succes, pos: Int) {
        GlobalConstants.OrderId=order.orderId
        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.orderDetailFragment, null)
    }
}
