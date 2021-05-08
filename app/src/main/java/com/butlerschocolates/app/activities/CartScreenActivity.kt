package com.butlerschocolates.app.activities

import android.content.Intent
import android.os.Bundle

import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.LinearLayoutManager
import com.butlerschocolates.app.R
import com.butlerschocolates.app.adapter.cart.CartProductAdapter
import com.butlerschocolates.app.base.BaseActivity
import com.butlerschocolates.app.databinding.ActivityCartScreenBinding
import com.butlerschocolates.app.database.dbmodel.ComplementaryItemsDB
import com.butlerschocolates.app.database.dbmodel.OptionAttribute
import com.butlerschocolates.app.database.dbmodel.ProductOptionItem
import com.butlerschocolates.app.global.GlobalConstants
import com.butlerschocolates.app.global.GlobalConstants.Request_Code_Cart_Screen
import com.butlerschocolates.app.util.Utilities


class CartScreenActivity : BaseActivity(), CartProductAdapter.CartProductClickedListener {

    var binding: ActivityCartScreenBinding? = null

    var displayProductCartArray: ArrayList<ProductOptionItem>? = null
    var optionAttribute: ArrayList<OptionAttribute>? = null
    var totalCarPrice: Double = 0.0

    var currencySymbol: String = ""
    var cartProductAdapter: CartProductAdapter? = null
    var isQtyUpdate = false
    var isPaymentButtonClicked: String? = "NO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_screen)

        // to prevent screen close when user clicked or touch ouside the screen
        setFinishOnTouchOutside(false)
        setScreenBottom()
        getAllProducts()
        checkCartSize()
        setTotalCartPrice()

        binding!!.btPayNow.setOnClickListener {
            var utilities = Utilities(this)
            if (utilities!!.readPref("isLogin").equals("true")) {
                if (displayProductCartArray!!.size > 0) {
                    isPaymentButtonClicked = "YES"
                    onBackPressed()
                } else {
                    utilities!!.showAlert("Product", "Please add products into your Butlers bag")
                }
            } else
                openBootStrapProcesScreen()
        }
    }

    private fun openBootStrapProcesScreen() {
        val intent = Intent(this@CartScreenActivity, BootStrapProcessActivity::class.java)
        intent.putExtra("redirectBackToCartScreen", "true");
        startActivityForResult(intent, GlobalConstants.REQUEST_CODE_LOGIN);
    }

    private fun setTotalCartPrice() {
        binding!!.totalCartPrice.setText(currencySymbol + showTwoDecimalPos(calculateTotalCartPrice()))
    }

    private fun checkCartSize() {
        if (displayProductCartArray!!.size > 0)
            setupCartProductPrecyclerView()
        else {
            binding!!.emptyCartLayout.visibility = View.VISIBLE
            binding!!.productCartRecycleview.visibility = View.GONE
            binding!!.btPayNow.visibility = View.INVISIBLE
        }
    }

    private fun calculateTotalCartPrice(): Double {
        var totalProductPrice = 0.0
        var totalOptionPrice = 0.0
        totalCarPrice = 0.0

        for (i in 0..displayProductCartArray!!.size - 1) {
            totalProductPrice = displayProductCartArray!!.get(i).price
            totalOptionPrice = 0.0

            for (j in 0..displayProductCartArray!!.get(i).optionItem.size - 1) {
                totalOptionPrice =
                    totalOptionPrice + displayProductCartArray!!.get(i).optionItem.get(j).option_price
            }
            totalCarPrice =
                totalCarPrice + (totalOptionPrice + totalProductPrice) * displayProductCartArray!!.get(
                    i
                ).product_qty
        }

        return totalCarPrice
    }

    private fun getAllProducts() {
        val productCursor = databaseHelper!!.selectProducts()
        displayProductCartArray = ArrayList<ProductOptionItem>()

        while (productCursor.moveToNext()) {
            var optionsCursor = databaseHelper!!.selectProductOptions(
                productCursor.getInt(
                    productCursor.getColumnIndex("cart_id")
                )
            )
            optionAttribute = ArrayList<OptionAttribute>()
            currencySymbol = productCursor.getString(productCursor.getColumnIndex("cur_symbol"))

            if (productCursor.getInt(productCursor.getColumnIndex("productOptionsSize")) >= 1) {
                while (optionsCursor.moveToNext()) {
                    var optionsItemCursor = databaseHelper!!.selectOptionsItem(
                        optionsCursor.getInt(
                            optionsCursor.getColumnIndex("option_id")
                        )
                    )
                    while (optionsItemCursor.moveToNext()) {
                        optionAttribute!!.add(
                            OptionAttribute(
                                optionsItemCursor.getInt(optionsItemCursor.getColumnIndex("attribute_id")),
                                optionsItemCursor.getInt(optionsItemCursor.getColumnIndex("patt_id")),
                                optionsItemCursor.getDouble(optionsItemCursor.getColumnIndex("option_price")),
                                optionsItemCursor.getString(optionsItemCursor.getColumnIndex("option_value"))
                            )
                        )
                    }
                }
            }

            var is_complementary =
                productCursor.getInt(productCursor.getColumnIndex("ComplementaryProductStatus"))
            var complementaryItemsList = ArrayList<ComplementaryItemsDB>()

            if (is_complementary == 1) {
                var complementaryItemCursor = databaseHelper!!.selectComplementaryItemswithId(
                    productCursor.getInt(productCursor.getColumnIndex("cart_id"))
                )

                while (complementaryItemCursor.moveToNext()) {
                    complementaryItemsList.add(
                        ComplementaryItemsDB(
                            productCursor.getInt(productCursor.getColumnIndex("cart_id")),
                            productCursor.getInt(productCursor.getColumnIndex("product_id")),
                            -1,  // -1 means value not fetch  from complemtary_info table as there is no need of at this moment
                            -1, // -1 means value not fetch  from complemtary_info table as there is no need of at this moment
                            complementaryItemCursor!!.getString(complementaryItemCursor.getColumnIndex("value")),
                            complementaryItemCursor!!.getInt(complementaryItemCursor.getColumnIndex("value_id")),
                            complementaryItemCursor!!.getString(complementaryItemCursor.getColumnIndex("image"))
                        )
                    )
                }
            }

            // add product
            displayProductCartArray!!.add(
                ProductOptionItem(
                    productCursor.getInt(productCursor.getColumnIndex("cart_id")),
                    productCursor.getString(productCursor.getColumnIndex("product_title")),
                    productCursor.getInt(productCursor.getColumnIndex("product_id")),
                    productCursor.getInt(productCursor.getColumnIndex("category_id")),
                    productCursor.getInt(productCursor.getColumnIndex("product_qty")),
                    productCursor.getInt(productCursor.getColumnIndex("product_max_qty")),
                    productCursor.getInt(productCursor.getColumnIndex("subCategoryId")),
                    productCursor.getString(productCursor.getColumnIndex("productImage")),
                    productCursor.getInt(productCursor.getColumnIndex("store_id")),
                    productCursor.getString(productCursor.getColumnIndex("store_title")),
                    productCursor.getString(productCursor.getColumnIndex("description")),
                    productCursor.getDouble(productCursor.getColumnIndex("price")),
                    productCursor.getInt(productCursor.getColumnIndex("productOptionsSize")),
                    productCursor.getString(productCursor.getColumnIndex("cur_symbol")),
                    optionAttribute!!,
                    complementaryItemsList
                )
            )
        }
    }

    private fun setupCartProductPrecyclerView() {
        var layoutManager =
            LinearLayoutManager(this@CartScreenActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.productCartRecycleview.setLayoutManager(layoutManager)

        cartProductAdapter = CartProductAdapter(
            this@CartScreenActivity,
            displayProductCartArray!! as ArrayList<ProductOptionItem>,
            this
        )
        binding!!.productCartRecycleview!!.setAdapter(cartProductAdapter)
    }

    //set screen Bottom
    fun setScreenBottom() {
        val window = window
        val width = (getResources().getDisplayMetrics().widthPixels * 0.90).toInt()
        window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        val wlp = window.attributes
        wlp.y = 100;
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
    }

    override fun deleteCartProduct(pos: Int) {
        isQtyUpdate = true
        databaseHelper!!.deleteProduct(displayProductCartArray!!.get(pos).cartId)
        displayProductCartArray!!.removeAt(pos)
        cartProductAdapter!!.notifyDataSetChanged()
        checkCartSize()
        setTotalCartPrice()

        //if (displayProductCartArray!!.size == 0)
          //  onBackPressed()
    }

    override fun updateCartProductQty(pos: Int, actionType: String) {

        if ((displayProductCartArray!!.get(pos).product_qty < displayProductCartArray!!.get(pos).product_max_qty) && actionType.equals(
                "plus"
            )
        ) {
            isQtyUpdate = true
            displayProductCartArray!!.get(pos).product_qty =
                displayProductCartArray!!.get(pos).product_qty + 1
        } else if (actionType.equals("minus")) {
            if (displayProductCartArray!!.get(pos).product_qty > 1) {
                displayProductCartArray!!.get(pos).product_qty =
                    displayProductCartArray!!.get(pos).product_qty - 1
                isQtyUpdate = true
            }
        } else {
            Utilities(this)!!.showAlert(
                "Product",
                "Maximum of " + displayProductCartArray!!.get(
                    pos
                ).product_max_qty + " can be added"
            )
        }

        databaseHelper!!.updateProductQtyWithCartId(
            displayProductCartArray!!.get(pos).cartId,
            displayProductCartArray!!.get(pos).product_qty
        )

        cartProductAdapter!!.notifyDataSetChanged()

        setTotalCartPrice()
    }


    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent()
        intent.putExtra("isQtyUpdate", "" + isQtyUpdate)
        intent.putExtra("isPaymentButtonClicked", isPaymentButtonClicked)
        setResult(Request_Code_Cart_Screen, intent)
        finish()
    }
}