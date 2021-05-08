package com.butlerschocolates.app.fragment.store

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.storelist.StoreListAdapter
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentStoreListBinding
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.model.storelist.Store
import com.butlerschocolates.app.model.storelist.StoreListRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.storelist.StoreListViewModel
import com.butlerschocolates.app.CupAdapter
import java.util.*


class StoreListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
    StoreListAdapter.StoreClickedListener {

      // Recycleview
      //https://android.jlelse.eu/instagram-style-navigation-using-navigation-component-854037cf1389

    var layoutManager: LinearLayoutManager? = null

    var storeListViewModel: StoreListViewModel? = null

    var storelist: ArrayList<Store>? = null
    var storeAdapter: StoreListAdapter? = null

    var binding: FragmentStoreListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_store_list, container, false)
        val view: View = binding!!.getRoot()

        intView()
        setBannerData()
        setupStoreRecylerView()
        setupViewModel()
        hideAndShowProgressBar()
        callStoreListApi()

      //  SavedAddressDialog(requireContext()).showAddressPopup()

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setBannerData() {

        binding!!.bannerData = utilities!!.getHomeApiData()

        if (utilities!!.readPref("isLogin").equals("true"))
            binding!!.textUserName.setText("Welcome Back \n \n " + utilities!!.getCustomerDetail().customerName)
        else
            binding!!.textUserName.setText("Welcome Back \n \n" + "Guest")
    }

    private fun intView() {
        utilities = Utilities(activity as MainActivity)
        binding!!.swipeRefreshLayout.setOnRefreshListener(this)
    }

    // setup store list Recyclerview
    private fun setupStoreRecylerView() {
        storelist = ArrayList()
        layoutManager = LinearLayoutManager(activity)
        binding!!.recylerViewStoreList!!.layoutManager = layoutManager
        storeAdapter =
            StoreListAdapter(
                requireActivity(),
                storelist as ArrayList<Store>
                , this
            )
        binding!!.recylerViewStoreList!!.adapter = storeAdapter
    }

    // hide an show progress
    private fun hideAndShowProgressBar() {
        storeListViewModel!!.progressbarObservable!!.observe(requireActivity(),
            Observer<Boolean> { progressObserve ->
                if (progressObserve!!) {
                    (activity as MainActivity).progressbar!!.show()
                } else {
                    (activity as MainActivity).progressbar!!.hide()
                }
            })
    }

    // call store list api
    private fun callStoreListApi() {

        storeListViewModel!!.onStoreListApiRequest(requireContext(), createStoreListRequestBody())!!
            .observe(
                requireActivity(),
                Observer {
                    binding!!.swipeRefreshLayout.isRefreshing = false

                    if (it!!.getData() != null) {

                        if(it!!.getData().code==301) {
                            (activity as MainActivity).redirectNotificationFragement()
                        }
                        else
                        {
                            setupLoyalty(it!!.getData()!!.data.loyalty as Loyalty)
                            // call is successful
                            if (it.getData().data.stores.size > 0) {
                                storelist!!.addAll(it.getData().data.stores)
                                storeAdapter!!.notifyDataSetChanged()

                            } else {
                                Console.Log("Tag", "Tag" + it.getData().data.error)
                            }
                        }
                    } else {
                        // call failed.
                        utilities!!.showAlert("Error", utilities!!.apiAlert(it.error).toString())
                    }
                }
            )
    }

    private fun setupLoyalty(loyalty: Loyalty) {

        var layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.recylerviewLoyalty!!.setLayoutManager(layoutManager)

        var cupAdapter = CupAdapter((activity as MainActivity).ComputeLoyalty(loyalty), loyalty)
        binding!!.recylerviewLoyalty!!.setAdapter(cupAdapter)
    }

    private fun setupViewModel() {
        storeListViewModel = ViewModelProviders.of(this).get(StoreListViewModel::class.java)
        storeListViewModel!!.init()
    }

    private fun createStoreListRequestBody(): StoreListRequestBody {
        var storeListRequestBody = StoreListRequestBody()
        storeListRequestBody.version = AppConstants.API_VERSION
        storeListRequestBody.auth_token = utilities!!.readPref("Auth_Token")
        return storeListRequestBody
    }

    override fun onRefresh() {
        storelist!!.clear()
        storeAdapter!!.notifyDataSetChanged()
        callStoreListApi()
    }

    override fun onStoreLocationsSelected(store: Store, pos: Int) {

        val bundle = Bundle()
        bundle.putParcelable(GlobalConstants.STORE_LIST_TAG, store)
        GlobalConstants.categoryId=store.category.get(0).categoryId

        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.action_storeListFragment_to_storeDetailFragment, bundle)
    }
}