package com.finalproject.profitableshopping.data.api

import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.HashMap

interface ShoppingApi {

    //category methods
    @GET("categories.php")
    fun getCategories(): Call<List<Category>>
    @GET("categories.php")
    fun getCategoryById(@Query("id") query: Int): Call<Category>
    @GET("categories.php")
    fun getCategoryByName(@Query("name") name: String): Call<Category>
    @FormUrlEncoded
    @POST("categories.php")
    fun addCategory(@Field("name")name:String):Call<String>
    @POST("categories.php")

    fun updateCategory(@Query("id") id:Int, @Body category: HashMap<String, String>):Call<String>
    @DELETE("categories.php?")
    fun deleteCategory(@Query("id")id:String):Call<String>


    //product methods

    @GET("products.php")
    fun getProducts(): Call<List<Product>>
    @GET("products.php?")
    fun getUserProducts(@Query("user_id") userId: Int): Call<List<Product>>
    @GET("products.php?")
    fun getProduct(@Query("id") proId: Int): Call<Product>
    @POST("products.php")
    fun addProduct(@Body product: HashMap<String, Any>):Call<Int>
    @POST("products.php?{id}")
    fun updateProduct(@Path("id") id:Int, @Body category: HashMap<String, String>):Call<String>
    @DELETE("products.php?{id}")
    fun deleteProduct(@Path("id")id:Int):Call<String>
    @Multipart
    @POST("products.php?{p_id}?{u_id}")
   fun uploadImage(@Part file:MultipartBody.Part,@Part ("file") name:RequestBody
                   ,@Path("p_id")pId:Int,@Path("u_id")uId:Int) :Call<Unit>



}