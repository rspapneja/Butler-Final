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
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentOrderDetailBinding
import com.butlerschocolates.app.database.DatabaseHelper
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.ItemsDB
import com.butlerschocolates.app.database.dbmodel.OptionDB
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Product
import com.butlerschocolates.app.model.orderInfo.OrderInfoRequestBody
import com.butlerschocolates.app.model.orderInfo.Success
import com.butlerschocolates.app.model.reorder.ReorderRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.orderInfo.OrderInfoViewModel
import com.butlerschocolates.app.Adapters.MyOrdersProductsAdapter

class OrderDetailFragment : BaseFragment() {

    var TAG = "Tag OrderDetailFragment"

    var linearLayoutManager: LinearLayoutManager? = null
    var productsAdapter: MyOrdersProductsAdapter? = null

     // view Model
    val orderViewModel: OrderInfoViewModel by viewModels()

    // data binding
    var binding: FragmentOrderDetailBinding? = null

    var success: Success? = null
    var products: ArrayList<Product>? = null

    // both array used for local db
    var SelectedOptionsItems: ArrayList<OptionDB>? = null
    var itemsArray: ArrayList<ItemsDB>? = null
    var  selectedComplementaryItems = ArrayList<ComplementaryItemsDB>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, container, false)
        val view: View = binding!!.getRoot()

        setViewModel()
        getOrderDetailApi()

        binding!!.btReorder.setOnClickListener {
           // checkStoreType()
            callReOrderApi()
        }
        return view
    }

    private fun callReOrderApi() {

        orderViewModel.setIsLoading(true, requireActivity())
        orderViewModel.reOrderRequest(creatteReOrderRequestBody())

        orderViewModel.reorderResponse.observe(viewLifecycleOwner, Observer { orderInfoData ->

        orderViewModel.setIsLoading(false, requireActivity())

            when (orderInfoData!!.status) {
                Status.SUCCESS -> {
                    if (orderInfoData.data!!.code == 1) {                        
                        products = orderInfoData.data.data.products as ArrayList<Product>
                        checkStoreType()
                        }
                    else if (orderInfoData!!.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(orderInfoData!!.data!!.data.error)
                    }
                    else if(orderInfoData!!.data!!.code==301) {
                        (activity as MainActivity).redirectNotificationFragement()
                    }
                    else
                        utilities!!.showAlert("Error", orderInfoData.data!!.data.error)
                }

                Status.ERROR -> {
                    utilities!!.showAlert("Error", utilities!!.apiAlert(orderInfoData.throwable!!))
                    Console.Log(TAG, orderInfoData.throwable.toString())
                }
            }
        })
    }

    private fun reorder()
    {
        databaseHelper=
            DatabaseHelper(requireContext())
        databaseHelper!!.emptyCart()

        for (i in 0..(products!!.size - 1)) {
            // options array start
            SelectedOptionsItems = ArrayList<OptionDB>()
            for (j in 0..(products!!.get(i).options.size - 1)) {

                // item array start
                itemsArray = ArrayList<ItemsDB>()
                for (k in 0..(products!!.get(i).options[j].items.size - 1)) {
                    itemsArray!!.add(
                        ItemsDB(
                            products!!.get(i).options[j].items[k].pattid,
                            products!!.get(i).options[j].items[k].value,
                            products!!.get(i).options[j].items[k].price
                        )
                    )
                }  //  end item array

                SelectedOptionsItems!!.add(
                    OptionDB(
                        products!!.get(i).options.get(j).title,
                        products!!.get(i).options.get(j).aid,
                        products!!.get(i).options.get(j).image,
                        itemsArray!!,-1
                    )
                )
            } // end option array

            addNewProduct(
                products!!.get(i).pid,
                products!!.get(i).title,
                -1,
                products!!.get(i).qty,
                products!!.get(i).maxQuantity,
                -1,
                products!!.get(i).image,
                success!!.store_id,
                success!!.storeTitle,
                products!!.get(i).desc,
                if (products!!.get(i).options.size != 0) 1 else 0,
                success!!.currency_symbol,
                products!!.get(i).price,
                SelectedOptionsItems!!,
               false,
                selectedComplementaryItems!!

            )
        }

        goToOrderPaymentScreen()
    }

    private fun goToOrderPaymentScreen() {
        Navigation.findNavController(requireActivity(), R.id.nav_host_container).navigate(R.id.orderPaymentFragment, null)
    }

    private fun getOrderDetailApi() {

        orderViewModel.setIsLoading(true, requireActivity())
        orderViewModel.getOrderStatusInfo(creatteOrderInfoRequestBody())

        orderViewModel.orderStatusResponse.observe(viewLifecycleOwner, Observer { orderInfoData ->
            orderViewModel.setIsLoading(false, requireActivity())
            when (orderInfoData!!.status) {
                Status.SUCCESS -> {
                    if (orderInfoData.data!!.code == 1) {
                        binding!!.data = orderInfoData.data.data.success
                        success = orderInfoData.data.data.success
                        binding!!.executePendingBindings()
                        binding!!.tvStoreTime.visibility=View.VISIBLE
                        setMyOrderRecycleview()
                    }
                    else if (orderInfoData!!.data!!.code == 4) {
                        (activity as MainActivity).handleAuthTokenAlert(orderInfoData!!.data!!.data.error)
                    }
                    else
                        utilities!!.showAlert("Error", orderInfoData.data!!.data.error)
                }

                Status.ERROR -> {
                    utilities!!.showAlert("Error", utilities!!.apiAlert(orderInfoData.throwable!!))
                    Console.Log(TAG, orderInfoData.throwable.toString())
                }
            }
        })
    }

    private fun setViewModel() {
        binding!!.orderViewModel = orderViewModel
    }

    private fun setMyOrderRecycleview() {
        linearLayoutManager = LinearLayoutManager(activity)
        binding!!.orderProducts.setLayoutManager(linearLayoutManager)

        productsAdapter = MyOrdersProductsAdapter(requireActivity(),success!!.products as ArrayList<Product>,success!!.currency_symbol)
        binding!!.orderProducts.setAdapter(productsAdapter)
    }

    private fun creatteOrderInfoRequestBody(): OrderInfoRequestBody {
        var requestBody = OrderInfoRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.order_id = GlobalConstants.OrderId
        return requestBody
    }

    private fun creatteReOrderRequestBody(): ReorderRequestBody {
        var requestBody = ReorderRequestBody()
        requestBody.version = AppConstants.API_VERSION
        requestBody.auth_token = utilities!!.readPref("Auth_Token")
        requestBody.order_id = GlobalConstants.OrderId
        return requestBody
    }

    fun checkStoreType() {
        if (databaseHelper!!.CheckIsStoreIdAlreadyInDBorNot(success!!.store_id)) {
            utilities!!.showAlertWithActions(
                "Confirmation", getString(R.string.store_different_alert), "YES", "NO",
                object : Utilities.ActionButtons {
                    override fun okAction() {
                        databaseHelper!!.emptyCart()
                        reorder()
                    }
                    override fun cancelAction() {}
                })
        } else {
            reorder()
        }
    }
}
