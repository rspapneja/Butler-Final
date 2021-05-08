package com.butlerschocolates.app.fragment.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentOrderStatusBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.orderInfo.Data
import com.butlerschocolates.app.model.orderInfo.OrderInfoRequestBody
import com.butlerschocolates.app.respostiory.feedback.FeedbackApiResponse
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.viewmodels.orderInfo.OrderInfoViewModel


class OrderStatusFragment : BaseFragment() {

    var binding:FragmentOrderStatusBinding?=null
    var orderViewModel: OrderInfoViewModel? = null

    var TAG="Tag OrderStatusFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_status, container, false)
        val view: View = binding!!.getRoot()

        setViewModel()
        getOrderStatusApi()

        binding!!.orderDetailLayout.setOnClickListener {
         Navigation.findNavController(requireActivity(), R.id.nav_host_container).navigate(R.id.orderDetailFragment, null)
        }

        return  view
    }

    private fun getOrderStatusApi() {
       orderViewModel!!.getOrderStatusInfo(creatteOrderInfoRequestBody())
       orderViewModel!!.orderStatusResponse.observe(viewLifecycleOwner, Observer { orderInfoData->
            when (orderInfoData!!.status) {
                Status.LOADING->{
                  orderViewModel!!.setIsLoading(true,requireActivity())
                }
                Status.SUCCESS-> {
                    orderViewModel!!.setIsLoading(false, requireActivity())

                    if (orderInfoData!!.data!!.code == 1) {
                        if(orderInfoData!!.data!!.data.success.orderStatusId==2||orderInfoData!!.data!!.data.success.orderStatusId==3)
                        {
                            this.binding!!.data = orderInfoData!!.data!!.data.success
                            binding!!.executePendingBindings()
                        }
                        else
                        {
                            binding!!.pickUpTimeLayout.visibility=View.INVISIBLE
                            this.binding!!.data = orderInfoData!!.data!!.data.success
                            binding!!.executePendingBindings()
                        }
                    }
                    else if (orderInfoData!!.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(orderInfoData!!.data!!.data.error)
                    }
                    else if(orderInfoData!!.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                     else
                        utilities!!.showAlert("Error",orderInfoData!!.data!!.data.error)
                 }
                Status.ERROR->{
                    orderViewModel!!.setIsLoading(false,requireActivity())
                    utilities!!.showAlert("Error",utilities!!.apiAlert(orderInfoData.throwable!!))
                    Console.Log(TAG,orderInfoData!!.throwable.toString())
                }
            }
        })
    }

    private fun setViewModel()
    {
        orderViewModel = ViewModelProviders.of(this).get(OrderInfoViewModel::class.java)
        binding!!.orderViewModel=orderViewModel
    }

    private fun creatteOrderInfoRequestBody(): OrderInfoRequestBody {
        var requestBody = OrderInfoRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token =  utilities!!.readPref("Auth_Token")
        requestBody.order_id =  GlobalConstants.OrderId
       // requestBody.check_payment =  1
        return requestBody
    }
}
