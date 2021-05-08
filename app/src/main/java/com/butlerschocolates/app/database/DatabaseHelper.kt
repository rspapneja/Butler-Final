package com.butlerschocolates.app.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.butlerschocolates.app.util.Console


class DatabaseHelper(internal var context: Context) : SQLiteOpenHelper(context,
    DB_NAME, null,
    DB_VERSION
) {

    val TAG = "Tag DatabaseHelper"
    private val PRODUCT_TABLE = "products"
    private val PRODUCTS_OPTIONS_TABLE = "products_options"
    private val OPTIONS_ITEMS_TABLE= "options_items"
    private val COMPLEMENTARY_ITEMS_TABLE= "Complementary_items"
    private val COMPLEMENTARY_INFO_TABLE= "Complementary_info"

    private val SQL_PRODUCTS = "CREATE TABLE " + PRODUCT_TABLE + "(cart_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "product_title varchar(150)," +
            "product_id integer," +
            "category_id integer," +
            "product_qty integer," +
            "product_max_qty integer," +
            "subCategoryId integer," +
            "productImage text," +
            "store_id integer," +
            "store_title text," +
            "description text," +
            "price real,"+
            "productOptionsSize integer," +
            "ComplementaryProductStatus integer," +
            "cur_symbol varchar(5))";

    private val SQL_OPTIONS_ITEMS = "CREATE TABLE " + OPTIONS_ITEMS_TABLE + "(product_item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "option_id INTEGER," +
            "attribute_id INTEGER," +
            "cart_id INTEGER," +
            "patt_id varchar(50)," +
            "option_price varchar(50)," +
            "option_value varchar(50))"

    private val SQL_PRODUCTS_OPTIONS = "CREATE TABLE " + PRODUCTS_OPTIONS_TABLE + "(option_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "attribute_id INTEGER," +
            "cart_id INTEGER," +
            "product_id INTEGER," +
            "option_name varchar(50)," +
            "options_image text)"

    private val SQL_COMPLEMENTARY_INFO = "CREATE TABLE " + COMPLEMENTARY_INFO_TABLE + "(complementary_info_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cart_id INTEGER," +
            "product_id INTEGER," +
            "aid INTEGER," +
            "title varchar(50)," +
            "is_multi_select INTEGER," +
            "restrict_attributes INTEGER," +
            "is_required INTEGER," +
            "isoption_expanded INTEGER," +
            "image varchar(50))"

    private val SQL_COMPLEMENTARY_ITEMS = "CREATE TABLE " + COMPLEMENTARY_ITEMS_TABLE + "(complementary_items_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cart_id INTEGER," +
            "complementary_info_id INTEGER," +
            "product_id INTEGER," +
            "aid INTEGER," +
            "value varchar(50)," +
            "value_id INTEGER," +
            "image text)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_PRODUCTS)
        db.execSQL(SQL_OPTIONS_ITEMS)
        db.execSQL(SQL_PRODUCTS_OPTIONS)
        db.execSQL(SQL_COMPLEMENTARY_INFO)
        db.execSQL(SQL_COMPLEMENTARY_ITEMS)

        Console.Log("DATABASE", "Database Created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE)
        db.execSQL("DROP TABLE IF EXISTS " + SQL_COMPLEMENTARY_INFO)
        db.execSQL("DROP TABLE IF EXISTS " + SQL_COMPLEMENTARY_ITEMS)
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_OPTIONS_TABLE)
        db.execSQL("DROP TABLE IF EXISTS " + OPTIONS_ITEMS_TABLE)

        //--create new tables
        onCreate(db)
    }

    fun insertProduct(contentValues: ContentValues): Long {
        val db = writableDatabase
        return db.insert(PRODUCT_TABLE, null, contentValues)
    }

    fun insertProductOption(contentValues: ContentValues): Long {
        val db = writableDatabase
        return db.insert(PRODUCTS_OPTIONS_TABLE, null, contentValues)
    }

    fun insertOptionItems(contentValues: ContentValues): Long {
        val db = writableDatabase
        return db.insert(OPTIONS_ITEMS_TABLE, null, contentValues)
    }

    fun insertComplementaryItems(contentValues: ContentValues): Long {
        val db = writableDatabase
        return db.insert(COMPLEMENTARY_ITEMS_TABLE, null, contentValues)
    }

    fun insertComplementaryInfo(contentValues: ContentValues): Long {
        val db = writableDatabase
        return db.insert(COMPLEMENTARY_INFO_TABLE, null, contentValues)
    }

    fun selectProducts(): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $PRODUCT_TABLE", null)
        return c
    }

    fun selectProductOptions(cart_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $PRODUCTS_OPTIONS_TABLE WHERE cart_id = $cart_id", null)
        return c
    }

    fun CheckIsStoreIdAlreadyInDBorNot(store_id: Int): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("select * from  $PRODUCT_TABLE", null)
        if (cursor.count <= 0) {
            cursor.close()
           return false
        }
        else{
                cursor.moveToFirst()
                var b= (store_id!=cursor.getInt(cursor.getColumnIndex("store_id")))
                cursor.close()
                return  b
        }
   }

    fun selectProductWithCardId(cart_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $PRODUCT_TABLE WHERE cart_id = $cart_id", null)
        return c
    }

    fun selectOptionsItem(option_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $OPTIONS_ITEMS_TABLE WHERE option_id = $option_id", null)
        return c
    }


    fun selectOptionsItemWithCarID(cart_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $OPTIONS_ITEMS_TABLE WHERE cart_id = $cart_id", null)
        return c
    }

    fun selectProductsFromId(productId: Int,store_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $PRODUCT_TABLE WHERE product_id = $productId AND store_id = $store_id", null)
        return c
    }

    fun selectProductsWithComplementaryStatus(productId: Int,complementaryProductStatus: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $PRODUCT_TABLE WHERE product_id = $productId AND ComplementaryProductStatus = $complementaryProductStatus", null)
        return c
    }

    fun selectComplementaryItemswithId(cart_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $COMPLEMENTARY_ITEMS_TABLE WHERE cart_id = $cart_id", null)
        return c
    }

    fun selectProductsFromId1(productId: Int, optionSize: Int, ComplementaryProductStatus:Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery(
            "select * from  $PRODUCT_TABLE WHERE product_id = $productId AND productOptionsSize= $optionSize AND ComplementaryProductStatus = $ComplementaryProductStatus",
            null
        )
        return c
    }

    fun checkProductExistOrNot(productId: Int): Int {
        var countValue: Int = 0
        val db = writableDatabase
        val c = db.rawQuery("select * from  $PRODUCT_TABLE WHERE product_id = $productId", null)
        return countValue
    }

    fun checkCartProduct(productId: Int, categoryId: Int, storeId: Int): Int {
        var countValue: Int = 0
        val db = writableDatabase
        val c = db.rawQuery("select * from  $PRODUCT_TABLE WHERE product_id = $productId AND category_id = $categoryId AND store_id = $storeId", null)

         if (c.count > 0) {
            while (c.moveToNext()) {
                var value = c.getInt(c.getColumnIndex("product_qty"))
                countValue += value
            }
        }
        return countValue
    }

    fun updateQty(product_id: Int, qty: Int) {
        val db = writableDatabase
        var content: ContentValues = ContentValues()
        content.put("product_qty", qty)
        db.update(PRODUCT_TABLE, content, "product_id=" + product_id, null)
    }

    fun updateProductQtyWithCartId(cart_id: Int, qty: Int) {
        val db = writableDatabase
        var content: ContentValues = ContentValues()
        content.put("product_qty", qty)
        db.update(PRODUCT_TABLE, content, "cart_id=" + cart_id, null)
    }

    fun getSingleProductQty(productId: Int,categoryId:Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $PRODUCT_TABLE WHERE product_id = $productId AND category_id=$categoryId", null)
        return c
    }

    fun getTotalProductsQty(): Int {
        val db = readableDatabase
        val cur = db.rawQuery("select SUM(product_qty) from  $PRODUCT_TABLE", null)

        if(cur.moveToFirst())
        {
            return cur.getInt(0)
        }
        else return 0
    }

    fun checkProductOptionAttributes1(cart_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $OPTIONS_ITEMS_TABLE WHERE cart_id = $cart_id", null)
        return c
    }

    fun checkComplementaryProducts(cart_id: Int): Cursor {
        val db = readableDatabase
        val c = db.rawQuery("select * from  $COMPLEMENTARY_ITEMS_TABLE WHERE cart_id = $cart_id", null)
        return c
    }

    fun deleteProduct(cart_id: Int): Boolean {
        val db = writableDatabase
        deleteOptionsItems(cart_id)
        deleteProductOption(cart_id)
        deleteComplementaryInfo(cart_id)
        deleteComplementaryItems(cart_id)
        return db.delete(PRODUCT_TABLE, "cart_id" + "=" + cart_id, null) > 0
    }

    fun deleteProductOption(cart_id: Int): Boolean {
        val db = writableDatabase
        return db.delete(PRODUCTS_OPTIONS_TABLE, "cart_id" + "=" + cart_id, null) > 0
    }

    fun deleteOptionsItems(cart_id: Int): Boolean {
        val db = writableDatabase
        return db.delete(OPTIONS_ITEMS_TABLE, "cart_id" + "=" + cart_id, null) > 0
    }

    fun deleteComplementaryInfo(cart_id: Int): Boolean {
        val db = writableDatabase
        return db.delete(COMPLEMENTARY_INFO_TABLE, "cart_id" + "=" + cart_id, null) > 0
    }

    fun deleteComplementaryItems(cart_id: Int): Boolean {
        val db = writableDatabase
        return db.delete(COMPLEMENTARY_ITEMS_TABLE, "cart_id" + "=" + cart_id, null) > 0
    }

    fun emptyCart(): Int {
        val db = writableDatabase
        val pr = db.delete(PRODUCT_TABLE, "1", null)
        val po = db.delete(PRODUCTS_OPTIONS_TABLE, "1", null)
        val oi = db.delete(OPTIONS_ITEMS_TABLE, "1", null)
        val cinfo = db.delete(COMPLEMENTARY_INFO_TABLE, "1", null)
        val citems = db.delete(COMPLEMENTARY_ITEMS_TABLE, "1", null)
        return pr + po + oi+cinfo+citems
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "ButlerDataBase"
    }
}
