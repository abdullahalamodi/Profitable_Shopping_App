package com.finalproject.profitableshopping.data.api

import com.finalproject.profitableshopping.data.models.Category

import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.HashMap

interface ShoppingApi {

    //category methods
    @GET("categories.php")
    fun fetchAllCategories(): Call<List<Category>>
    @GET("categories.php?")
    fun fetchCategory(@Query("id") query: Int): Call<Category>
    @POST("categories.php")
    fun addCategory(@Body category: HashMap<String, Any>):Call<String>
    @POST("categories.php?{id}")
    fun editCategory( @Path("id") id:Int,@Body category: HashMap<String, Any>):Call<String>
    @DELETE("categories.php?{id}")
    fun deleteCategory(@Path("id")id:Int):Call<String>


    //product methods

}