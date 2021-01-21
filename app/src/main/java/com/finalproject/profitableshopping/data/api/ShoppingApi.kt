package com.finalproject.profitableshopping.data.api

import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Comment
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
   // @FormUrlEncoded
    @POST("categories.php")
    fun addCategory(@Body category:Category):Call<String>
    @POST("categories.php")
    fun updateCategory(@Query("id") id:String, @Body category: Category):Call<String>
    @DELETE("categories.php?")
    fun deleteCategory(@Query("id")id:String):Call<String>


    //product methods

    @GET("products.php")
    fun getProducts(): Call<List<Product>>
    @GET("products.php")
    fun getUserProducts(@Query("user_id") userId: String): Call<List<Product>>
    @GET("products.php")
    fun getProduct(@Query("id") proId: String): Call<Product>
    @POST("products.php")
    fun addProduct(@Body product: Product):Call<String>
    @POST("products.php")
    fun updateProduct(@Query("id") id:String, @Body product: Product):Call<String>
    @DELETE("products.php")
    fun deleteProduct(@Query("id")id:String):Call<String>

    @Multipart
    @POST("images.php")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("product_id") desc: RequestBody
    ): Call<String>


    // comment method

    @GET("comments.php")
    fun getComments(): Call<List<Comment>>
    @GET("comments.php")
    fun getUserComments(@Query("user_id") userId: String): Call<List<Comment>>
    @GET("comments.php")
    fun getComment(@Query("id") commentId: String): Call<Comment>
    @POST("comments.php")
    fun addComment(@Body comment: Comment):Call<String>
    @POST("comments.php")
    fun updateComment(@Query("id") id:String, @Body comment: Comment):Call<String>
    @DELETE("comments.php")
    fun deleteComment(@Query("id")id:String):Call<String>
}