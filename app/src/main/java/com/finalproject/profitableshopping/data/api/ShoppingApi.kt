package com.finalproject.profitableshopping.data.api

import com.finalproject.profitableshopping.data.models.CategoryModel

import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface ShoppingApi {

    @GET("categories.php")
    fun fetchAllCaregories(): Call<List<CategoryModel>>
    @GET("categories.php?")
    fun fetchCaregory(@Query("id") query: Int): Call<CategoryModel>
    @POST("categories.php")
    fun createCategory(@Body category: HashMap<Any, Any>):Call<String>
    @POST("categories.php?{id}")
    fun editCategory( @Path("id") id:Int,@Body category: HashMap<Any, Any>):Call<String>
    @DELETE("categories.php?{id}")
    fun deleteCategory(@Path("id")id:Int):Call<String>
}