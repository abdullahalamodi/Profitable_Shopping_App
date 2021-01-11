package com.finalproject.profitableshopping.data.api

import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Product

import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.HashMap

interface ShoppingApi {

    //category methods
    @GET("categories.php")
    fun getCategories(): Call<List<Category>>
    @GET("categories.php?")
    fun getCategoryById(@Query("id") query: Int): Call<Category>
    @GET("categories.php?")
    fun getCategoryByName(@Query("name") name: String): Call<Category>
    @POST("categories.php")
    fun addCategory(@Body category: HashMap<String, Any>):Call<String>
    @POST("categories.php?{id}")
    fun updateCategory(@Path("id") id:Int, @Body category: HashMap<String, Any>):Call<String>
    @DELETE("categories.php?{id}")
    fun deleteCategory(@Path("id")id:Int):Call<String>


    //product methods


    @GET("products.php")
    fun getProducts(): Call<List<Product>>
    @GET("products.php?")
    fun getUserProducts(@Query("user_id") userId: Int): Call<List<Product>>
    @GET("products.php?")
    fun getProduct(@Query("id") proId: Int): Call<Product>
    @POST("products.php")
    fun addProduct(@Body product: HashMap<String, Any>):Call<String>
    @POST("products.php?{id}")
    fun updateProduct(@Path("id") id:Int, @Body category: HashMap<String, Any>):Call<String>
    @DELETE("products.php?{id}")
    fun deleteProduct(@Path("id")id:Int):Call<String>



}