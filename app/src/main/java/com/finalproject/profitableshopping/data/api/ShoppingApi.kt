package com.finalproject.profitableshopping.data.api

import com.finalproject.profitableshopping.data.models.CategoryModel

import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface ShoppingApi {

    @GET("")
    fun fetchAllCaregories(): Call<List<CategoryModel>>
    @GET("")
    fun fetchCaregory(@Query("id") query: Int): Call<CategoryModel>
    @POST()
    fun createCategory(@Body category: HashMap<Any, Any>):Call<String>
    @POST("{id}")
    fun editCategory( @Path("id") id:Int,@Body category: HashMap<Any, Any>):Call<String>
    @DELETE("{id}")
    fun deleteCategory(@Path("id")id:Int):Call<String>
}