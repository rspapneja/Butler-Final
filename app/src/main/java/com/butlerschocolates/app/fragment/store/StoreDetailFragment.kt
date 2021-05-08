package com.butlerschocolates.app.fragment.store

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.storedetail.CategoryAdapter
import com.butlerschocolates.app.adapter.storedetail.MenuAdapter
import com.butlerschocolates.app.adapter.storedetail.SubCategoryAdapter
import com.butlerschocolates.app.application.ButlersApplication
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.callback.OptionsCheckerListerner

import com.butlerschocolates.app.databinding.FragmentStoreDetailBinding
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.OptionDB
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.model.storedetail.Menu
import com.butlerschocolates.app.model.storedetail.StoreDetailRequestBody
import com.butlerschocolates.app.model.storelist.Category
import com.butlerschocolates.app.model.storelist.Store
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.sharedViewModel.SharedViewModel
import com.butlerschocolates.app.viewmodels.storedetail.StoreDetailViewModel
import com.butlerschocolates.app.CupAdapter


class StoreDetailFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
    CategoryAdapter.CategorySelectedListerner, OptionsCheckerListerner {

    var binding: FragmentStoreDetailBinding? = null

    var layoutManager: LinearLayoutManager? = null
    var storedetail: Store? = null

    var loyalty: Loyalty? = null

    var storeDetailViewModel: StoreDetailViewModel? = null


    // menu
    var menu: ArrayList<Menu>? = null
    var menuAdapter: MenuAdapter? = null

    var currencyType: String? = ""
    var storeBannerImage: String? = ""

    // category
    var categoryAdapter: CategoryAdapter? = null
    var categoryId: Int = 0
    var selectedCategoryPostion: Int = 0

    var subCategoryAdapter: SubCategoryAdapter? = null

    var sharedViewModel: SharedViewModel ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_store_detail, container, false)
        val view: View = binding!!.getRoot()

        intView()
        getAreguments()
        setupcategoryRecyclerView()
        setupViewModel()

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateCartValue()
    }

    private fun intView() {
        utilities = Utilities(activity as MainActivity)
        binding!!.swipeRefreshLayout.setOnRefreshListener(this)
        menu = ArrayList<Menu>()
    }

    // get argument
    private fun getAreguments() {
        if (this.arguments != null)
            storedetail =
                this.requireArguments().getParcelable<Store>(GlobalConstants.STORE_LIST_TAG)
    }

    // setup recycleview of top categorey of banner

    private fun setupcategoryRecyclerView() {
        layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.recyclerViewCategory!!.setLayoutManager(layoutManager)

        storedetail!!.category.get(selectedCategoryPostion).isSelected = 1

        // category adpter
        categoryAdapter =
            CategoryAdapter(
                requireActivity(),
                selectedCategoryPostion,
                storedetail!!.category as ArrayList<Category>,
                this
            )
        binding!!.recyclerViewCategory!!.setAdapter(categoryAdapter)
    }

    // setup the model
    private fun setupViewModel() {
        // initailize the view mdel
        storeDetailViewModel = ViewModelProviders.of(this).get(StoreDetailViewModel::class.java)
        storeDetailViewModel!!.init()

        // link view mdel with xml binding
        binding!!.viewModel = storeDetailViewModel!!
    }

    // call store list api
    private fun callStoreDetailApi(categoryId: String) {

        //  Check internet connection
        if (ButlersApplication.instance!!.isConnected) {
            storeDetailViewModel!!.storeDetailApiRequest(
                createStoreDetailRequestBody(categoryId),
                requireActivity()
            )!!.observe(
                requireActivity(),
                Observer {
                    if (isAttachedToActivity()) {
                        if (it!!.getData() != null) {

                            if(it!!.getData().code==301) {
                                (activity as MainActivity).redirectNotificationFragement()
                            }
                            else {
                                // call is successful
                                loyalty = it!!.getData().data.loyalty
                                setupLoyalty(loyalty!!)
                                currencyType = it.data.data.currency_symbol
                                storeBannerImage = it.data.data.bannerImg

                                menu = it.data.data.menu!! as ArrayList<Menu>

                                if (it.data.data.isExpandable == 0)
                                    setMenuRecyclerView(it.data.data.currency_symbol)
                                else
                                    setSubCategoryRecycleview(
                                        it.data.data.currency_symbol
                                    )
                         }
                        } else {
                            binding!!.swipeRefreshLayout.isRefreshing = false
                            storeDetailViewModel!!.setIsLoading(false, requireActivity())
                            // call failed.
                            utilities!!.showAlert(
                                "Error",
                                utilities!!.apiAlert(it.error).toString()
                            )
                        }
                        // hide progressbar
                        binding!!.swipeRefreshLayout.isRefreshing = false
                        storeDetailViewModel!!.setIsLoading(false, requireActivity())
                    }
                }
            )
        } else {
            utilities!!.showAlert("Error", getString(R.string.no_internetconnection))
        }
    }

    // set Sub Category
    private fun setSubCategoryRecycleview(currencyType: String) {

        // update product quanity
        for (i in 0..(menu!!.size - 1)) {
            for (j in 0..(menu!!.get(i).products!!.size - 1)) {
                var productsC = databaseHelper!!.selectProductsFromId(
                    menu!!.get(i).products!!.get(j).pid,
                    storedetail!!.storeId.toInt()
                )
                if (productsC.count > 0) {
                    while (productsC.moveToNext()) {
                        menu!!.get(i).products!!.get(j).qty =
                            menu!!.get(i).products!!.get(j).qty + productsC.getInt(
                                productsC.getColumnIndex(
                                    "product_qty"
                                )
                            )
                    }
                }
            }
        }

        if (isAttachedToActivity()) {
            var layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding!!.recyclerMenu!!.setLayoutManager(layoutManager)

            //  set sub category Adapter
            subCategoryAdapter = SubCategoryAdapter(requireContext(), menu!!, currencyType, this)
            binding!!.recyclerMenu!!.setAdapter(subCategoryAdapter)
        }
    }

    // set up Menu list
    private fun setMenuRecyclerView(currencyType: String) {

        // update product quanity
        for (i in 0..(menu!!.size - 1)) {
            var productsC = databaseHelper!!.selectProductsFromId(
                menu!!.get(i).pid,
                storedetail!!.storeId.toInt()
            )

            if (productsC.count > 0) {
                while (productsC.moveToNext()) {
                    menu!!.get(i).qty =
                        menu!!.get(i).qty + productsC.getInt(productsC.getColumnIndex("product_qty"))
                }
            }
        }

        if (isAttachedToActivity()) {
            // set menu adpter
            var layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding!!.recyclerMenu.setLayoutManager(layoutManager)
            menuAdapter =
                MenuAdapter(requireContext(), menu!!, currencyType, this)
            binding!!.recyclerMenu.setAdapter(menuAdapter)
        }
    }

    // set up loyalty
    private fun setupLoyalty(loyalty: Loyalty) {
        if (isAttachedToActivity()) {
            var layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding!!.recylerviewLoyalty.setLayoutManager(layoutManager)

            //  set loyalty on adapter
            var cupAdapter = CupAdapter((activity as MainActivity).ComputeLoyalty(loyalty), loyalty)
            binding!!.recylerviewLoyalty.setAdapter(cupAdapter)
        }
    }

    //  create store detail request body
    fun createStoreDetailRequestBody(categoryId: String): StoreDetailRequestBody {
        var storeDetailRequestBody = StoreDetailRequestBody()
        storeDetailRequestBody.version = AppConstants.API_VERSION
        storeDetailRequestBody.store_id = storedetail!!.storeId
        storeDetailRequestBody.category_id = categoryId
        storeDetailRequestBody.auth_token = utilities!!.readPref("Auth_Token")
        return storeDetailRequestBody
    }

    override fun onRefresh() {
        menu!!.clear()
        if (menuAdapter != null)
            menuAdapter!!.notifyDataSetChanged()
        if (subCategoryAdapter != null)
            subCategoryAdapter!!.notifyDataSetChanged()

        callStoreDetailApi(GlobalConstants.categoryId.toString())
    }

    override fun onCategorySelected(position: Int) {
        categoryId = storedetail!!.category.get(position).categoryId
        selectedCategoryPostion = position
        GlobalConstants.categoryId = categoryId
        callStoreDetailApi(categoryId.toString())
    }

    fun openCustomiseYourProductScreen(menuPos: Int, productPostion: Int) {

        GlobalConstants.StoreBannerImage=storeBannerImage!!
        val bundle = Bundle()
        bundle.putParcelable(GlobalConstants.LOYALTY_TAG, loyalty)
        bundle.putParcelableArrayList(GlobalConstants.Menu_Tag, menu!! as ArrayList<Menu>)
        bundle.putInt(GlobalConstants.menuPos_Tag, menuPos)
        bundle.putInt(GlobalConstants.productPos_Tag, productPostion)
        bundle.putString(GlobalConstants.CurrencyType, currencyType)
        bundle.putParcelable(GlobalConstants.StoreDetail_Tag, storedetail)

        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.action_storeDetailFragment_to_customiseYourProductFragment, bundle)
    }

    // add category and products
    override fun insertProduct(view: AppCompatTextView, pos: Int) {

     // options and attributes not  available
       if (menu!!.get(pos).options!!.size == 0) {

         // Checking  is_complimentary product
             if(menu!!.get(pos).is_complimentary) {

                utilities!!.showAlertWithActions(
                     "Complimentary", getString(R.string.complememtiary_alert)+" "+menu!!.get(pos).complimentary!!.title+" ?", "YES", "NO",
           object : Utilities.ActionButtons {
               override fun okAction() {
                   openComplementaryProductScreen(pos,-1)
               }
               override fun cancelAction() {
                   insertProductWithoutSubCategory(pos)
               }
           })
       }
       else{
           insertProductWithoutSubCategory(pos)
       }

        } else {
           // options and attributes available
             openCustomiseYourProductScreen(pos, -1)
        }
        (activity as MainActivity).updateCartValue()
    }

    private fun openComplementaryProductScreen(menuPos: Int,productPos: Int) {

        GlobalConstants.StoreBannerImage=storeBannerImage!!

        val bundle = Bundle()
        bundle.putParcelable(GlobalConstants.LOYALTY_TAG, loyalty)
        bundle.putParcelableArrayList(GlobalConstants.Menu_Tag, menu!! as ArrayList<Menu>)
        bundle.putInt(GlobalConstants.menuPos_Tag, menuPos)
        bundle.putInt(GlobalConstants.productPos_Tag, productPos)
        bundle.putString(GlobalConstants.CurrencyType, currencyType)
        bundle.putParcelable(GlobalConstants.StoreDetail_Tag, storedetail)

        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.chooseComplimentaryProductFragment, bundle)
    }

    private fun insertProductWithoutSubCategory( pos:Int) {
        var  SelectedOptionsItems = ArrayList<OptionDB>()
        var  selectedComplementaryItems = ArrayList<ComplementaryItemsDB>()

       var b= inserProductWithoutComplementaryProducts(
            menu!!.get(pos).pid,
            menu!!.get(pos).title,
            categoryId,
            1,
            menu!!.get(pos).maxQuantity,
            0,
            menu!!.get(pos).image,
            storedetail!!.storeId.toInt(),
           storedetail!!.title,
            menu!!.get(pos).desc,
            menu!!.get(pos).options!!.size,
            currencyType!!,
            menu!!.get(pos).price,
            SelectedOptionsItems,
            false,
            selectedComplementaryItems!!
        )
        if(b) {
            menu!!.get(pos).qty = menu!!.get(pos).qty + 1;
            menuAdapter!!.notifyDataSetChanged()
            (activity as MainActivity).updateCartValue()
        }
    }

    // add category ,subcategory and products
    override fun insertProduct(view: AppCompatTextView, menuPos: Int, productPos: Int) {

   if (menu!!.get(menuPos).products!!.get(productPos).options.size == 0) {
      // Checking  is_complimentary product
            if(menu!!.get(menuPos).products!!.get(productPos).is_complimentary) {
               utilities!!.showAlertWithActions(
                    "Complimentary", getString(R.string.complememtiary_alert)+" "+menu!!.get(menuPos).products!!.get(productPos).complimentary!!.title+" ?", "YES", "NO",
                    object : Utilities.ActionButtons {
                        override fun okAction() {
                            openComplementaryProductScreen(menuPos, productPos)
                        }
                        override fun cancelAction() {
                           insertProductWithSubCategory(menuPos,productPos)
                        }
                    })
            }
            else{
                insertProductWithSubCategory(menuPos,productPos)
            }
        } else {
            // options and attributes available
            openCustomiseYourProductScreen(menuPos, productPos)
        }
    }

    private fun insertProductWithSubCategory(menuPos: Int,productPos: Int) {
        var SelectedOptionsItems = ArrayList<OptionDB>()
        var  selectedComplementaryItems = ArrayList<ComplementaryItemsDB>()

      var b=inserProductWithoutComplementaryProducts(
            menu!!.get(menuPos).products!!.get(productPos).pid,
            menu!!.get(menuPos).products!!.get(productPos).title,
            categoryId,
            1,
            menu!!.get(menuPos).products!!.get(productPos).maxQuantity,
            menu!!.get(menuPos).subcategoryId,
            menu!!.get(menuPos).products!!.get(productPos).image,
            storedetail!!.storeId.toInt(),
            storedetail!!.title,
            menu!!.get(menuPos).products!!.get(productPos).desc,
            menu!!.get(menuPos).products!!.get(productPos).options.size,
            currencyType!!,
            menu!!.get(menuPos).products!!.get(productPos).price,
            SelectedOptionsItems,
            false,
            selectedComplementaryItems!!)

        if(b) {
            menu!!.get(menuPos).products!!.get(productPos).qty =  menu!!.get(menuPos).products!!.get(productPos).qty + 1;
            subCategoryAdapter!!.notifyDataSetChanged()
            (activity as MainActivity).updateCartValue()
        }
    }

    override fun checkStoreType(productQtyView: AppCompatTextView, menuPos: Int, productPos: Int) {
       var optionSize=0
         if(productPos==-1)
             optionSize=menu!!.get(menuPos).options!!.size
         else
             optionSize=menu!!.get(menuPos).products!!.get(productPos).options.size

        if (optionSize == 0) {
            if (databaseHelper!!.CheckIsStoreIdAlreadyInDBorNot(storedetail!!.storeId.toInt())) {
                utilities!!.showAlertWithActions(
                    "Confirmation", getString(R.string.store_different_alert), "YES", "NO",
                    object : Utilities.ActionButtons {
                        override fun okAction() {
                            databaseHelper!!.emptyCart()
                            decisionInsertProductType(productQtyView, menuPos, productPos)
                        }
                        override fun cancelAction() {}
                    })
            } else {
                decisionInsertProductType(productQtyView, menuPos, productPos)
            }
        }
        else
        {
            decisionInsertProductType(productQtyView, menuPos, productPos)
        }
    }

    fun decisionInsertProductType(view: AppCompatTextView, menuPos: Int, productPos: Int) {
        if (productPos == -1)
            insertProduct(view, menuPos)
        else
            insertProduct(view, menuPos, productPos)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GlobalConstants.Request_Code_Cart_Screen) {
            if (data!!.getStringExtra("isQtyUpdate").toString().equals("true")) {
                menu!!.clear()
                if (menuAdapter != null)
                    menuAdapter!!.notifyDataSetChanged()
                if (subCategoryAdapter != null)
                    subCategoryAdapter!!.notifyDataSetChanged()
                callStoreDetailApi(categoryId.toString())
            }
        }
    }
}