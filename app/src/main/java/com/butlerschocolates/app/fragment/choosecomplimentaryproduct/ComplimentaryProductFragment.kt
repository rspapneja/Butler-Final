package com.butlerschocolates.app.fragment.choosecomplimentaryproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.complementaryproducts.ComplementaryProductsAdapter
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.callback.IOnBackPressed
import com.butlerschocolates.app.databinding.FragmentChooseComplimentaryProductBinding
import com.butlerschocolates.app.database.DatabaseHelper
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.ComplementaryProductItem
import com.butlerschocolates.app.database.dbmodel.OptionDB
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Complimentary
import com.butlerschocolates.app.model.common.Items
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.model.common.Option
import com.butlerschocolates.app.model.storedetail.Menu
import com.butlerschocolates.app.model.storelist.Store
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.viewmodels.sharedViewModel.SharedViewModel
import com.butlerschocolates.app.CupAdapter


class ComplimentaryProductFragment : BaseFragment(), IOnBackPressed,
    ComplementaryProductsAdapter.ComplementaryListener {
    var binding: FragmentChooseComplimentaryProductBinding? = null
    var options: ArrayList<Option>? = null
    var loyalty: Loyalty? = null
    var complimentary: Complimentary? = null

    var productName: String? = null
    var CurrencyType: String? = null

    var menuPos: Int = 0
    var productPosition: Int = 0
    var menu: Menu? = null

    var TAG = "Tag CustomiseYourProductFragment"

    var SelectedOptionsItems: ArrayList<OptionDB>? = ArrayList()

    var storeInfo: Store? = null

    var categoryId: Int = 0

    val db: DatabaseHelper? = null

    var myCondition = true
    var sharedViewModel: SharedViewModel? = null

    var selectedComplementaryItems: ArrayList<ComplementaryItemsDB>? = null

    var complementaryProductsAdapter: ComplementaryProductsAdapter? = null
    var complimentary_items: ArrayList<Items>? = null

    var screenType = 0
    // 0 means directly open complementary screen without any intermediate screen
    // 1 means intermediate screen open like customise your Prdouct then open complementary product screen

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choose_complimentary_product,
            container,
            false
        )

        val view: View = binding!!.getRoot()

        getAreguments()

        setBannerInfo()

        setupLoyalty(loyalty!!)

        setComplementaryRecycleview()

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel!!.data.observe(viewLifecycleOwner, Observer {
            Console.Toast(requireContext(), it.toString())
        })

        binding!!.addTocart.setOnClickListener {

            if (selectedComplementaryProductCount() == 0) {

                utilities!!.showAlertWithActions(
                    "Complimentary", createAlertMessage(), "YES", "NO",
                    object : Utilities.ActionButtons {
                        override fun okAction() {
                            if (getSelectedComplementaryItems(false)) {
                                myCondition = false
                                (activity as MainActivity).onBackPressed()
                            } else myCondition = true
                        }

                        override fun cancelAction() {
                            myCondition = false
                            (activity as MainActivity).onBackPressed()
                        }
                    })
            } else {
                myCondition = false
                if (getSelectedComplementaryItems(true)) {
                    (activity as MainActivity).onBackPressed()
                } else myCondition = true
            }
        }

        binding!!.addTocart1.setOnClickListener {
            myCondition = false
            selectedComplementaryItems = ArrayList<ComplementaryItemsDB>()
            if (getSelectedComplementaryItems(false)) {
                (activity as MainActivity).onBackPressed()
            } else myCondition = true
        }
        return view
    }

    private fun createAlertMessage(): String {

        if (productPosition == -1) {
            return getString(R.string.complememtiary_without_alert) + " " + menu!!.title.toLowerCase() + " without complimentary " + menu!!.complimentary!!.title.toLowerCase()+ " ?"

        } else {
            return getString(R.string.complememtiary_without_alert) + " " + menu!!.products!!.get(
                productPosition
            ).title.toLowerCase() + " without complimentary " + menu!!.products!!.get(productPosition).complimentary!!.title.toLowerCase() + " ?"
        }
    }

    fun selectedComplementaryProductCount(): Int {
        var count = 0
        for (i in 0 until complimentary!!.items!!.size) {
            if (complimentary!!.items!!.get(i).isSelected == 1) {
                count++
            }
        }

        return count!!
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateCartValue()
    }

    private fun getSelectedComplementaryItems(complementaryStatus: Boolean): Boolean {
        selectedComplementaryItems = ArrayList<ComplementaryItemsDB>()
        for (i in 0 until complimentary!!.items!!.size) {
            if (complimentary!!.items!!.get(i).isSelected == 1) {
                selectedComplementaryItems!!.add(
                    ComplementaryItemsDB(
                        -1,  //
                        menu!!.pid,
                        -1,  //
                        complimentary!!.aid,
                        complimentary!!.items!![i].value,
                        complimentary!!.items!![i].valueId,
                        complimentary!!.items!![i].image
                    )
                )
            }
        }

        complementaryProductItem = ComplementaryProductItem(
            -1,
            menu!!.pid,
            complimentary!!.aid,
            complimentary!!.title,
            complimentary!!.isMultiSelect,
            complimentary!!.restrictAttributes,
            complimentary!!.isRequired,
            complimentary!!.isoptionExpanded,
            complimentary!!.image,
            selectedComplementaryItems!!
        )

        return checkProductTypeWithoutComplementary(complementaryStatus)

        // checkProductType()
    }

    private fun checkProductTypeWithoutComplementary(complementaryStatus: Boolean): Boolean {
        //screenType= 0 means directly open complementary screen without any intermediate screen
        // screenType = 1 means intermediate screen open like customise your Prdouct then open complementary product screen

        if (screenType == 1) {
            // add products with options attributes (without subcategory)
            if (productPosition == -1) {
                return addNewProduct(
                    menu!!.pid,
                    menu!!.title,
                    categoryId,
                    1,
                    menu!!.maxQuantity,
                    menu!!.subcategoryId,
                    menu!!.image,
                    storeInfo!!.storeId.toInt(),
                    storeInfo!!.title,
                    menu!!.desc,
                  //  menu!!.options!!.size,
                    if (menu!!.options!!.size != 0) 1 else 0,
                    CurrencyType!!,
                    menu!!.price,
                    SelectedOptionsItems!!
                    , complementaryStatus,
                    selectedComplementaryItems!!
                )
            }
            // add products with options attributes(with subcategory)
            else {
                return addNewProduct(
                    menu!!.products!!.get(productPosition).pid,
                    menu!!.products!!.get(productPosition).title,
                    categoryId,
                    1,
                    menu!!.products!!.get(productPosition).maxQuantity,
                    menu!!.subcategoryId,
                     menu!!.products!!.get(productPosition).image,
                    storeInfo!!.storeId.toInt(),
                    storeInfo!!.title,
                    menu!!.products!!.get(productPosition).desc,
                    if (menu!!.products!!.get(productPosition).options.size != 0) 1 else 0,
                    //menu!!.products!!.get(productPosition).options.size,
                    CurrencyType!!,
                    menu!!.products!!.get(productPosition).price,
                    SelectedOptionsItems!!
                    , complementaryStatus,
                    selectedComplementaryItems!!
                )
            }
        } else {
            // add products without options attributes(without subcategory)
            if (productPosition == -1) {
                return insertProductWithoutSubCategoryWithBase(
                    menu!!.pid,
                    menu!!.title,
                    categoryId,
                    1,
                    menu!!.maxQuantity,
                    0,
                    menu!!.image,
                    storeInfo!!.storeId.toInt(),
                    storeInfo!!.title,
                    menu!!.desc,
                    if (menu!!.options!!.size != 0) 1 else 0,
                  //  menu!!.options!!.size,
                    CurrencyType!!,
                    menu!!.price,
                    SelectedOptionsItems!!,
                    complementaryStatus,
                    selectedComplementaryItems!!
                )
            }
            // add products without options attributes(with subcategory)
            else {
                return insertProductWithoutSubCategoryWithBase(
                    menu!!.products!!.get(productPosition).pid,
                    menu!!.products!!.get(productPosition).title,
                    categoryId,
                    1,
                    menu!!.products!!.get(productPosition).maxQuantity,
                    menu!!.subcategoryId,
                    menu!!.products!!.get(productPosition).image,
                    storeInfo!!.storeId.toInt(),
                    storeInfo!!.title,
                    menu!!.products!!.get(productPosition).desc,
                    if (menu!!.products!!.get(productPosition).options.size != 0) 1 else 0,
                    //menu!!.products!!.get(productPosition).options.size,
                    CurrencyType!!,
                    menu!!.products!!.get(productPosition).price,
                    SelectedOptionsItems!!,
                    complementaryStatus,
                    selectedComplementaryItems!!
                )
            }
        }
    }

    private fun setupLoyalty(loyalty: Loyalty) {
        var layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL, false
        )
        binding!!.recylerviewLoyalty.setLayoutManager(layoutManager)

        var cupAdapter = CupAdapter((activity as MainActivity).ComputeLoyalty(loyalty), loyalty)
        binding!!.recylerviewLoyalty.setAdapter(cupAdapter)
    }

    private fun setBannerInfo() {
        binding!!.productName.setText("Choose your \n Complimentary " + complimentary!!.title)
        utilities!!.loadImage(complimentary!!.image, binding!!.bannerimage)
    }

    private fun getAreguments() {
        if (this.arguments != null)
            menuPos = this.requireArguments().getInt(GlobalConstants.menuPos_Tag)
        menu = this.requireArguments().getParcelableArrayList<Menu>(GlobalConstants.Menu_Tag)!!
            .get(menuPos)
        productPosition = this.requireArguments().getInt(GlobalConstants.productPos_Tag)

        if (requireArguments().containsKey("SelectedOptionsItems")) {
            SelectedOptionsItems =
                requireArguments().getParcelableArrayList<OptionDB>("SelectedOptionsItems")

            screenType = 1
        }

        if (productPosition == -1) {
            options = menu!!.options as ArrayList<Option>
            productName = menu!!.title
            complimentary = menu!!.complimentary
        } else {
            options = menu!!.products!!.get(productPosition).options as ArrayList<Option>
            productName = menu!!.products!!.get(productPosition)!!.title
            complimentary = menu!!.products!!.get(productPosition)!!.complimentary
        }

        loyalty = this.requireArguments().getParcelable<Loyalty>(GlobalConstants.LOYALTY_TAG)
        CurrencyType = this.requireArguments().getString(GlobalConstants.CurrencyType)
        storeInfo = this.requireArguments().getParcelable<Store>(GlobalConstants.StoreDetail_Tag)
        categoryId = GlobalConstants.categoryId
    }

    private fun setComplementaryRecycleview() {
        var layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL, false
        )

        binding!!.recyclerViewItems.setLayoutManager(layoutManager)
        complimentary_items = complimentary!!.items!! as ArrayList<Items>

        complementaryProductsAdapter = ComplementaryProductsAdapter(
            requireContext(), complimentary!!.items!! as ArrayList<Items>, this
        )

        binding!!.recyclerViewItems.setAdapter(complementaryProductsAdapter)
    }

    override fun onBackPressed(): Boolean {
        return if (myCondition) {

            //action not popBackStack
            utilities!!.showAlertWithActions(
                "Complimentary", createAlertMessage(), "YES", "NO",
                object : Utilities.ActionButtons {
                    override fun okAction() {
                        binding!!.addTocart1.performClick()
                    }

                    override fun cancelAction() {
                        myCondition = false
                        (activity as MainActivity).onBackPressed()
                    }
                })
            true
        } else {
            false
        }
    }

    override fun onItemSelected(pos: Int) {

        if (complimentary_items!!.get(pos).isSelected == 0) {
            complimentary_items!!.get(pos).isSelected = 1
        } else {
            complimentary_items!!.get(pos).isSelected = 0
        }
        complementaryProductsAdapter!!.notifyDataSetChanged()

        var count = 0

        for (i in 0..complimentary_items!!.size - 1) {
            if (complimentary_items!!.get(i).isSelected == 1) {
                count++
            }
        }

        if (complimentary!!.maxAllowed < count) {
            // if (1 < count) {
            utilities!!.showAlert(
                "Product",
                "Maximum of " + complimentary!!.maxAllowed + " can be added"
            )
            complimentary_items!!.get(pos).isSelected = 0
            complementaryProductsAdapter!!.notifyDataSetChanged()
        }
    }
}
