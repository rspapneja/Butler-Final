package com.butlerschocolates.app.fragment.customiseyourproduct

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.adapter.customizeyourproduct.OptionsAdapter
import com.butlerschocolates.app.base.BaseActivity
import com.butlerschocolates.app.base.BaseFragment
import com.butlerschocolates.app.databinding.FragmentCustomiseYourProductBinding
import com.butlerschocolates.app.database.DatabaseHelper
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.ItemsDB
import com.butlerschocolates.app.database.dbmodel.OptionDB
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.model.common.Complimentary
import com.butlerschocolates.app.model.common.Loyalty
import com.butlerschocolates.app.model.storedetail.Menu
import com.butlerschocolates.app.model.common.Option
import com.butlerschocolates.app.model.storelist.Store
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.util.Utilities
import com.butlerschocolates.app.CupAdapter

class CustomiseYourProductFragment : BaseFragment() {

    var binding: FragmentCustomiseYourProductBinding? = null

    var options: ArrayList<Option>? = null
    var loyalty: Loyalty? = null
    var complimentary: Complimentary? = null

    var productName: String? = null
    var CurrencyType: String? = null

    var menuPos: Int = 0
    var productPosition: Int = 0
    var menu: Menu? = null

    var menulist: ArrayList<Menu>? = null

    var TAG = "Tag CustomiseYourProductFragment"

    var SelectedOptionsItems: ArrayList<OptionDB>? = null
    var itemsArray: ArrayList<ItemsDB>? = null
    var storeInfo: Store? = null
    var categoryId: Int = 0

    val db: DatabaseHelper? = null

    var storeBannerImage: String? = ""
    var  selectedComplementaryItems = ArrayList<ComplementaryItemsDB>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_customise_your_product,
            container,
            false
        )
        val view: View = binding!!.getRoot()

        getAreguments()
        setBannerInfo()

        setupLoyalty(loyalty!!)

        setProductsOptionsRecycleview(options!!, CurrencyType!!)

        binding!!.addTocart.setOnClickListener {
            checkStoreType()
        }
        return view;
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).updateCartValue()
    }

    private fun getSelectedAddedDataIntoDatabase() {
        SelectedOptionsItems = ArrayList<OptionDB>()
        var SelectedOptionsItemsTemp = ArrayList<OptionDB>()
        var isRestrictError = false

        var restrictedItems= ArrayList<OptionDB>()
        var restrictedItemsTemp= ArrayList<OptionDB>()

        restrictedItems.clear()
        restrictedItemsTemp.clear()

        for (i in 0 until options!!.size) {
            itemsArray = ArrayList<ItemsDB>()
            var isOptionAdded = false

            for (j in options!!.get(i).items.indices) {
                if (options!!.get(i).items.get(j).isSlected == 1) {
                    isOptionAdded = true
                    itemsArray!!.add(ItemsDB(options!!.get(i).items.get(j).pattid, options!!.get(i).items.get(j).value, options!!.get(i).items.get(j).price))
                }
            }
            if(options!![i].isRequired==1){
                restrictedItems.add(OptionDB(options!![i].title,options!![i].aid,options!![i].image, emptyList(),options!![i].restrictAttributes))
                restrictedItemsTemp.add(OptionDB(options!![i].title,options!![i].aid,options!![i].image, emptyList(),0))
            }

            if (isOptionAdded) {
                SelectedOptionsItems!!.add(OptionDB(options!!.get(i).title, options!!.get(i).aid, options!!.get(i).image, itemsArray!!, itemsArray!!.size))
                SelectedOptionsItemsTemp!!.add(OptionDB(options!!.get(i).title, options!!.get(i).aid, options!!.get(i).image, emptyList(), 0))
            }
        }

        for (i in 0 until restrictedItemsTemp.size){
            if(SelectedOptionsItemsTemp.contains(restrictedItemsTemp[i])){
                var selectedItemIndex=SelectedOptionsItemsTemp!!.indexOf(restrictedItemsTemp[i])

                if(restrictedItems[i].restricted_value==0 && SelectedOptionsItems!![selectedItemIndex].restricted_value<1){
                    var size=if(restrictedItems!![i].restricted_value ==0) 1 else restrictedItems!![i].restricted_value
                    utilities!!.showAlert("Product", "Minimum " + size + " required from " + restrictedItems!![i].option_name)
                    isRestrictError = true
                    break
                }

                if(SelectedOptionsItems!![selectedItemIndex].restricted_value < restrictedItems[i].restricted_value){
                    var size=if(restrictedItems!![i].restricted_value ==0) 1 else restrictedItems!![i].restricted_value
                    utilities!!.showAlert("Product", "Minimum " + size + " required from " + restrictedItems!![i].option_name)
                    isRestrictError = true
                    break
                }
            }
            else{
                var size=if(restrictedItems!![i].restricted_value ==0) 1 else restrictedItems!![i].restricted_value
                utilities!!.showAlert("Product", "Minimum " + size + " required from " + restrictedItems!![i].option_name)
                isRestrictError = true
                break
            }
        }
        // isRestrictError is used every thing ok from restrict of attributes
        if (!isRestrictError) {
        var complimentaryStatus=false
        var alertMessage=""

            if (productPosition == -1) {
                complimentaryStatus=menu!!.is_complimentary
                alertMessage= getString(R.string.complememtiary_alert)+" "+menu!!.complimentary!!.title+" ?"

                Console.Log("if consale",alertMessage)

            }
            else{
                complimentaryStatus=menu!!.products!!.get(productPosition).is_complimentary
                alertMessage= getString(R.string.complememtiary_alert)+" "+menu!!.products!!.get(productPosition).complimentary!!.title+" ?"
                Console.Log("else consale",alertMessage)
            }

            if(complimentaryStatus) {
                utilities!!.showAlertWithActions(
                        "Complimentary", alertMessage, "YES", "NO",
                object : Utilities.ActionButtons {
                    override fun okAction() {
                        openComplementaryProductScreen(menuPos,productPosition)
                    }
                    override fun cancelAction() {
                        checkProductType()
                    }
                })
            }
            else{
                checkProductType()
            }
        }
    }

    fun openComplementaryProductScreen(menuPos: Int, productPostion: Int) {
        GlobalConstants.StoreBannerImage=storeBannerImage!!
        val bundle = Bundle()
        bundle.putParcelable(GlobalConstants.LOYALTY_TAG, loyalty)
        bundle.putParcelableArrayList(GlobalConstants.Menu_Tag, menulist as ArrayList<Menu>)
        bundle.putInt(GlobalConstants.menuPos_Tag, menuPos)
        bundle.putInt(GlobalConstants.productPos_Tag, productPostion)
        bundle.putString(GlobalConstants.CurrencyType, CurrencyType)
        bundle.putParcelable(GlobalConstants.StoreDetail_Tag, storeInfo)
        bundle.putParcelableArrayList("SelectedOptionsItems", SelectedOptionsItems!!)

        Navigation.findNavController(requireActivity(), R.id.nav_host_container)
            .navigate(R.id.chooseComplimentaryProductFragment, bundle, NavOptions.Builder()
                .setPopUpTo(R.id.customiseYourProductFragment,
                    true).build())
    }

    private fun checkProductType() {
        var b=false

        // add  product with category only
        // productPosition -1 means there is no subcategory
        if (productPosition == -1) {
            checkProduct(
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
                menu!!.options!!.size,
                CurrencyType!!,
                menu!!.price,
                SelectedOptionsItems!!
            )
        }
        // add  product with category and subcategory
        else {
            checkProduct(
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
                menu!!.products!!.get(productPosition).options.size,
                CurrencyType!!,
                menu!!.products!!.get(productPosition).price,
                SelectedOptionsItems!!
            )
        }
    }

    private fun setBannerInfo() {
        binding!!.productName.setText("Customise\n Your " + productName)

        utilities!!.loadImage(GlobalConstants.StoreBannerImage, binding!!.bannerimage)
    }

    private fun getAreguments() {
        if (this.arguments != null)
            menuPos = this.requireArguments().getInt(GlobalConstants.menuPos_Tag)
        menulist=this.requireArguments().getParcelableArrayList<Menu>(GlobalConstants.Menu_Tag)!!
        menu = this.requireArguments().getParcelableArrayList<Menu>(GlobalConstants.Menu_Tag)!!
            .get(menuPos)
        productPosition = this.requireArguments().getInt(GlobalConstants.productPos_Tag)

        if (productPosition == -1) {
            options = menu!!.options as ArrayList<Option>
            productName = menu!!.title
            complimentary=menu!!.complimentary
        } else {
            options = menu!!.products!!.get(productPosition).options as ArrayList<Option>
            productName = menu!!.products!!.get(productPosition)!!.title
            complimentary=menu!!.products!!.get(productPosition)!!.complimentary
        }

        loyalty = this.requireArguments().getParcelable<Loyalty>(GlobalConstants.LOYALTY_TAG)
        CurrencyType = this.requireArguments().getString(GlobalConstants.CurrencyType)
        storeInfo = this.requireArguments().getParcelable<Store>(GlobalConstants.StoreDetail_Tag)
        categoryId = GlobalConstants.categoryId
    }

    private fun setProductsOptionsRecycleview(options: ArrayList<Option>, currencyType: String) {
        var layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL, false
        )
        binding!!.recyclerViewItems.setLayoutManager(layoutManager)

        var optionsAdapter = OptionsAdapter(requireContext(), options, currencyType)
        binding!!.recyclerViewItems.setAdapter(optionsAdapter)
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

    fun checkProduct(
        pid: Int,
        title: String,
        categoryId: Int,
        product_qty: Int,
        maxQuantity: Int,
        subcategoryId: Int,
        image: String,
        storeId: Int,
        storeTitle:String,
        desc: String,
        optionSize: Int,
        currencyType: String,
        price: Double,
        selectedOptionsItems: ArrayList<OptionDB>
    ) {
        val db = (activity as BaseActivity).databaseHelper!!
     //   var productsC = db.selectProductsFromId(pid,storeInfo!!.storeId.toInt())
        var productsC = db!!.selectProductsFromId1(pid, selectedOptionsItems.size, 0)
        var qty = 0
        if (productsC.count == 0) {
            if( addNewProduct(
                pid,
                title,
                categoryId,
                1,
                maxQuantity,
                subcategoryId,
                image,
                storeId,
                storeTitle,
                desc,
                if (optionSize != 0) 1 else 0,
                currencyType!!,
                price,
                selectedOptionsItems!!,
                false,
                selectedComplementaryItems!!
            ))
            {
                (activity as MainActivity).onBackPressed()
            }
        } else {
            while (productsC.moveToNext()) {
                qty += productsC.getInt(productsC.getColumnIndex("product_qty"))
            }
            if (qty < maxQuantity) {
               if( addNewProduct(
                    pid,
                    title,
                    categoryId,
                    1,
                    maxQuantity,
                    subcategoryId,
                    image,
                    storeId,
                    storeTitle,
                    desc,
                    if (optionSize != 0) 1 else 0,
                    currencyType!!,
                    price,
                    selectedOptionsItems!!,
                    false,
                    selectedComplementaryItems!!
                ))
               {
                   (activity as MainActivity).onBackPressed()
               }

            } else {
                utilities!!.showAlert(
                    "Product",
                    "Maximum of " + maxQuantity + " can be added"
                )
            }
        }
    }

  fun checkStoreType() {
        if (databaseHelper!!.CheckIsStoreIdAlreadyInDBorNot(storeInfo!!.storeId.toInt())) {
            utilities!!.showAlertWithActions(
                "Confirmation", getString(R.string.store_different_alert), "YES", "NO",
                object : Utilities.ActionButtons {
                    override fun okAction() {
                        databaseHelper!!.emptyCart()
                        getSelectedAddedDataIntoDatabase()
                    }
                    override fun cancelAction() {}
                })
        } else {
            getSelectedAddedDataIntoDatabase()
        }
    }
}
