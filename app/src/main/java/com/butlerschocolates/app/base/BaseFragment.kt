package com.butlerschocolates.app.base

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.butlerschocolates.app.database.DatabaseHelper
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.ComplementaryProductItem
import com.butlerschocolates.app.database.dbmodel.OptionDB
import com.butlerschocolates.app.util.ProgressBarHandler
import com.butlerschocolates.app.util.Utilities

open class BaseFragment : Fragment() {

    var progressbar: ProgressBarHandler? = null
    var utilities: Utilities? = null
    var databaseHelper: DatabaseHelper? = null

    var complementaryProductItem: ComplementaryProductItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressbar = ProgressBarHandler(requireActivity());
        databaseHelper =
            DatabaseHelper(requireActivity())
        utilities = Utilities(requireActivity())
    }

    fun addFinalProduct(
        product_id: Int,
        productTitle: String,
        category_id: Int,
        product_qty: Int,
        product_max_qty: Int,
        subCategoryId: Int,
        productImage: String,
        store_id: Int,
        storeTitle:String,
        description: String,
        productOptionsSize: Int,
        cur_symbol: String,
        price: Double,
        options: ArrayList<OptionDB>,
        complementaryStatus: Boolean
    ) {
        // Direct Insert new product
        var contentValues = ContentValues()
        contentValues.put("product_title", productTitle)
        contentValues.put("product_id", product_id)
        contentValues.put("category_id", category_id)
        contentValues.put("product_qty", product_qty)
        contentValues.put("product_max_qty", product_max_qty)
        contentValues.put("subCategoryId", subCategoryId)
        contentValues.put("productOptionsSize", productOptionsSize)
        contentValues.put("productImage", productImage)
        contentValues.put("store_id", store_id)
        contentValues.put("store_title", storeTitle)
        contentValues.put("description", description)
        contentValues.put("price", price)
        contentValues.put("ComplementaryProductStatus", if (complementaryStatus) 1 else 0)
        contentValues.put("cur_symbol", cur_symbol)
        val cart_id: Long = databaseHelper!!.insertProduct(contentValues)

        Log.e("cart_id", "cart_id" + cart_id)

        if (productOptionsSize > 0) {
            for (i in 0 until options!!.size) {
                var optionsContentValues = ContentValues()
                optionsContentValues.put("cart_id", cart_id)
                optionsContentValues.put("attribute_id", options.get(i).attribute_id)
                optionsContentValues.put("option_name", options.get(i).option_name)
                optionsContentValues.put("product_id", product_id)
                optionsContentValues.put("options_image", options.get(i).Options_image)
                val option_id = databaseHelper!!.insertProductOption(optionsContentValues)

                for (j in options!![i].itemsDB.indices) {
                    var contentValues = ContentValues()
                    contentValues.put("cart_id", cart_id)
                    contentValues.put("option_id", option_id)
                    contentValues.put("attribute_id", options.get(i).attribute_id)
                    contentValues.put("patt_id", options.get(i).itemsDB.get(j).patt_id)
                    contentValues.put("option_price", options.get(i).itemsDB.get(j).option_price)
                    contentValues.put("option_value", options.get(i).itemsDB.get(j).option_value)
                    val items_id = databaseHelper!!.insertOptionItems(contentValues)
                }
            }
        }

        if (complementaryStatus) {
            var cv = ContentValues()
            cv.put("cart_id", cart_id)
            cv.put("product_id", product_id)
            cv.put("aid", complementaryProductItem!!.aid)
            cv.put("title", complementaryProductItem!!.title)
            cv.put("is_multi_select", complementaryProductItem!!.is_multi_select)
            cv.put("restrict_attributes", complementaryProductItem!!.restrict_attributes)
            cv.put("is_required", complementaryProductItem!!.aid)
            cv.put("isoption_expanded", complementaryProductItem!!.isoption_expanded)
            cv.put("image", complementaryProductItem!!.image)

            val complementary_info_id = databaseHelper!!.insertComplementaryInfo(cv)

            Log.e("complementary_info_id", "complementary_info_id=" + complementary_info_id);

            for (j in complementaryProductItem!!.complementaryItemsList.indices) {
                var contentValues = ContentValues()
                contentValues.put("cart_id", cart_id)
                contentValues.put("product_id", product_id)
                contentValues.put("aid", complementaryProductItem!!.aid)
                contentValues.put("complementary_info_id", complementary_info_id)
                contentValues.put(
                    "value",
                    complementaryProductItem!!.complementaryItemsList[j].value
                )
                contentValues.put(
                    "value_id",
                    complementaryProductItem!!.complementaryItemsList[j].value_id
                )
                contentValues.put(
                    "image",
                    complementaryProductItem!!.complementaryItemsList[j].image
                )
                val complementary_items_id =
                    databaseHelper!!.insertComplementaryItems(contentValues)

                Log.e("complementary_items_id", "complementary_items_id=" + complementary_items_id);
            }
        }
    }

    fun addNewProduct(
        product_id: Int,
        productTitle: String,
        category_id: Int,
        product_qty: Int,
        product_max_qty: Int,
        subCategoryId: Int,
        productImage: String,
        store_id: Int,
        storeTitle:String ,
        description: String,
        productOptionsSize: Int,
        cur_symbol: String,
        price: Double,
        options: ArrayList<OptionDB>,
        complementaryStatus: Boolean,
        selectedComplementaryItems: ArrayList<ComplementaryItemsDB>
    ):Boolean {
        var b=false
        var productCount = databaseHelper!!.selectProductsFromId1(product_id, productOptionsSize,if(complementaryStatus) 1 else 0)
        var cartIds = ArrayList<Int>()

        if (productCount.count == 0) {
            //=>insert product
            addFinalProduct(
                product_id,
                productTitle,
                category_id,
                product_qty,
                product_max_qty,
                subCategoryId,
                productImage,
                store_id,
                storeTitle ,
                description,
                productOptionsSize,
                cur_symbol,
                price,
                options,
                complementaryStatus
            )
            b= true
        } else {
            while (productCount.moveToNext()) {
                cartIds.add(productCount.getInt(productCount.getColumnIndex("cart_id")))
            }
        }

        if (cartIds.size != 0) {
            var found = false
            for (i in 0 until cartIds!!.size) {

                var attributeCursor = databaseHelper!!.checkProductOptionAttributes1(cartIds[i])
                var dbAttributeId = ArrayList<Int>()

                while (attributeCursor.moveToNext()) {
                    dbAttributeId.add(attributeCursor.getInt(attributeCursor.getColumnIndex("patt_id")))
                }

                var productAttributeId = ArrayList<Int>()
                for (i in 0 until options!!.size) {
                    for (j in options[i].itemsDB.indices) {
                        productAttributeId.add(options[i].itemsDB[j].patt_id)
                    }
                }

                if (dbAttributeId.size == productAttributeId.size) {
                    if (dbAttributeId.containsAll(productAttributeId)) {

                        if (complementaryStatus) {
                            if (checkComplementaryProducts(
                                    cartIds[i],
                                    selectedComplementaryItems!!
                                )
                            ) {
                                found = true
                                //=>Increase quantity
                                var productCursor =
                                    databaseHelper!!.selectProductWithCardId(cartIds[i])
                                var productQty = 0
                                if (productCursor.count != 0) {
                                    while (productCursor.moveToNext()) {
                                        productQty =
                                            productCursor.getInt(productCursor.getColumnIndex("product_qty"))
                                    }
                                    if (productQty != 0) {

                                        if (productQty < product_max_qty) {
                                            databaseHelper!!.updateProductQtyWithCartId(
                                                cartIds[i],
                                                productQty + 1
                                            )
                                         ///   (activity as MainActivity).onBackPressed()
                                            b= true
                                        } else {
                                            utilities!!.showAlert(
                                                "Product",
                                                "Maximum of " + product_max_qty + " can be added" )

                                            b= false
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            found = true
                        //=>Increase quantity
                            var productCursor =
                                databaseHelper!!.selectProductWithCardId(cartIds[i])
                            var productQty = 0
                            if (productCursor.count != 0) {
                                while (productCursor.moveToNext()) {
                                    productQty =
                                        productCursor.getInt(productCursor.getColumnIndex("product_qty"))
                                }
                                if (productQty != 0) {
                                    if (productQty < product_max_qty) {
                                        databaseHelper!!.updateProductQtyWithCartId(
                                            cartIds[i],
                                            productQty + 1
                                        )
                                    ///    (activity as MainActivity).onBackPressed()
                                        b= true
                                    } else {
                                        utilities!!.showAlert(
                                            "Product",
                                            "Maximum of " + product_max_qty + " can be added"
                                        )

                                        b= false
                                    }
                                }
                            }

                        }

                    }
                }
            }

            if (!found) {
                //=>add new product
                addFinalProduct(
                    product_id,
                    productTitle,
                    category_id,
                    product_qty,
                    product_max_qty,
                    subCategoryId,
                    productImage,
                    store_id,
                    storeTitle,
                    description,
                    productOptionsSize,
                    cur_symbol,
                    price,
                    options,
                    complementaryStatus
                )
                    b= true
             ///   (activity as MainActivity).onBackPressed()
            }
        }

        return b
    }

    fun insertProductWithoutSubCategoryWithBase(
        product_id: Int,
        productTitle: String,
        category_id: Int,
        product_qty: Int,
        product_max_qty: Int,
        subCategoryId: Int,
        productImage: String,
        store_id: Int,
        storeTitle:String,
        description: String,
        productOptionsSize: Int,
        cur_symbol: String,
        price: Double,
        options: ArrayList<OptionDB>,
        complementaryStatus: Boolean,
        selectedComplementaryItems: ArrayList<ComplementaryItemsDB>

    ):Boolean {
        var b= true
        if (complementaryStatus) {
            var productsC = databaseHelper!!.selectProductsFromId(
                product_id,
                store_id
            )

            var SelectedOptionsItems = options
            var cartIds = ArrayList<Int>()

            var productQty = 0

            if (productsC.count == 0) {
                addFinalProduct(
                    product_id,
                    productTitle,
                    category_id,
                    1,
                    product_max_qty,
                    subCategoryId,
                    productImage,
                    store_id,
                    storeTitle,
                    description,
                    productOptionsSize,
                    cur_symbol,
                    price,
                    SelectedOptionsItems,
                    complementaryStatus
                )
                b=true
              ///  (activity as MainActivity).onBackPressed()
            } else {
                while (productsC.moveToNext()) {
                    cartIds.add(productsC.getInt(productsC.getColumnIndex("cart_id")))
                }

                if ((cartIds.size != 0)) {
                    var found = false
                    for (i in 0 until cartIds!!.size) {
                        var complementaryCursor =
                            databaseHelper!!.checkComplementaryProducts(cartIds[i])
                        var dbComplementatyItemsValueId = ArrayList<Int>()

                        while (complementaryCursor.moveToNext()) {
                            dbComplementatyItemsValueId.add(
                                complementaryCursor.getInt(
                                    complementaryCursor.getColumnIndex("value_id")
                                )
                            )
                        }
                        var selectedComplementaryValueId = ArrayList<Int>()
                        for (i in 0 until selectedComplementaryItems!!.size) {
                            selectedComplementaryValueId.add(selectedComplementaryItems!![i].value_id)
                        }
                        if (dbComplementatyItemsValueId.size == selectedComplementaryValueId.size) {
                            if (dbComplementatyItemsValueId.containsAll(
                                    selectedComplementaryValueId
                                )
                            ) {
                                //=>Increase quantity
                                found = true

                                var productCursor =
                                    databaseHelper!!.selectProductWithCardId(cartIds[i])

                                if (productCursor.count != 0) {
                                    while (productCursor.moveToNext()) {
                                        productQty =
                                            productCursor.getInt(productCursor.getColumnIndex("product_qty"))
                                    }
                                    if (productQty < product_max_qty) {
                                        databaseHelper!!.updateProductQtyWithCartId(
                                            cartIds[i],
                                            productQty + 1
                                        )
                                        b=true
                                        //(activity as MainActivity).onBackPressed()
                                    } else {
                                        utilities!!.showAlert(
                                            "Product",
                                            "Maximum of " + product_max_qty + " can be added"
                                        )
                                        b=false
                                    }
                                }
                            } else productQty = 1
                        } else productQty = 1
                    }

                    if (!found) {
                        addFinalProduct(
                            product_id,
                            productTitle,
                            category_id,
                            productQty,
                            product_max_qty,
                            subCategoryId,
                            productImage,
                            store_id,
                            storeTitle,
                            description,
                            productOptionsSize,
                            cur_symbol,
                            price,
                            SelectedOptionsItems,
                            complementaryStatus
                        )
                ///        (activity as MainActivity).onBackPressed()
                        b=true
                    }
                }
            }
        } else {
            inserProductWithoutComplementaryProducts(
                product_id,
                productTitle,
                category_id,
                product_qty,
                product_max_qty,
                subCategoryId,
                productImage,
                store_id,
                storeTitle,
                description,
                productOptionsSize,
                cur_symbol,
                price,
                options,
                complementaryStatus,
                selectedComplementaryItems
            )
            b= true
        }
        return b
    }

    fun isAttachedToActivity(): Boolean {
        return isVisible && activity != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    fun inserProductWithoutComplementaryProducts(
        product_id: Int,
        productTitle: String,
        category_id: Int,
        product_qty: Int,
        product_max_qty: Int,
        subCategoryId: Int,
        productImage: String,
        store_id: Int,
        storeTitle:String,
        description: String,
        productOptionsSize: Int,
        cur_symbol: String,
        price: Double,
        SelectedOptionsItems: ArrayList<OptionDB>,
        complementaryStatus: Boolean,
        selectedComplementaryItems: ArrayList<ComplementaryItemsDB>
    ): Boolean {
        var b = false

        var productCursor =
            databaseHelper!!.selectProductsWithComplementaryStatus(product_id, 0)
        var productQty = 0
        if (productCursor.count != 0) {
            var cart_id = -1
            while (productCursor.moveToNext()) {
                productQty = productCursor.getInt(productCursor.getColumnIndex("product_qty"))
                cart_id = productCursor.getInt(productCursor.getColumnIndex("cart_id"))
            }

            if (productQty < product_max_qty) {
                databaseHelper!!.updateProductQtyWithCartId(cart_id, productQty + 1)
                b = true
            } else {
                utilities!!.showAlert("Product", "Maximum of " + product_max_qty + " can be added")
                b=false
            }
        } else {
            addFinalProduct(
                product_id,
                productTitle,
                category_id,
                1,
                product_max_qty,
                subCategoryId,
                productImage,
                store_id,
                storeTitle,
                description,
                productOptionsSize,
                cur_symbol,
                price,
                SelectedOptionsItems,
                complementaryStatus
            )
            b = true
        }
        return b
    }

    private fun checkComplementaryProducts(
        cart_id: Int,
        selectedComplementaryItems: ArrayList<ComplementaryItemsDB>
    ): Boolean {
        var complementaryCursor =
            databaseHelper!!.checkComplementaryProducts(cart_id)
        var dbComplementatyItemsValueId = ArrayList<Int>()

        while (complementaryCursor.moveToNext()) {
            dbComplementatyItemsValueId.add(
                complementaryCursor.getInt(
                    complementaryCursor.getColumnIndex("value_id")
                )
            )
        }

        var selectedComplementaryValueId = ArrayList<Int>()
        for (i in 0 until selectedComplementaryItems!!.size) {
            selectedComplementaryValueId.add(selectedComplementaryItems!![i].value_id)
        }

        if (dbComplementatyItemsValueId.size == selectedComplementaryValueId.size) {

            if (dbComplementatyItemsValueId.containsAll(
                    selectedComplementaryValueId
                )
            ) {
                return true
            } else return false
        } else {
            return false
        }
    }


    fun hideShowNoRecordFoundLayout(alertMessage:String, recyclerView: RecyclerView, imageView: AppCompatImageView, textView: TextView, layout: LinearLayout, resourceID:Int, visibilityStatus:Int)
    {
        textView.text=alertMessage
        layout.visibility= if(visibilityStatus==0) View.VISIBLE else View.GONE
        recyclerView.visibility= if(visibilityStatus==1) View.VISIBLE else View.GONE
        imageView.setImageResource(resourceID)
    }
}


