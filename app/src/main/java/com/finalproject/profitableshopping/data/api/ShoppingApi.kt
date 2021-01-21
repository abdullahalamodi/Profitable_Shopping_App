package com.finalproject.profitableshopping.data.api

import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderDetails
import com.finalproject.profitableshopping.data.models.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.http.*

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
    fun addCategory(@Body category: Category): Call<String>

    @POST("categories.php")
    fun updateCategory(@Query("id") id: String, @Body category: Category): Call<String>

    @DELETE("categories.php?")
    fun deleteCategory(@Query("id") id: String): Call<String>

    @Multipart
    @POST("images.php")
    fun uploadCategoryImage(
        @Part image: MultipartBody.Part,
        @Part("category_id") desc: RequestBody
    ): Call<String>

    //product methods
    @GET("products.php")
    fun getProducts(): Call<List<Product>>

    @GET("products.php")
    fun getUserProducts(@Query("user_id") userId: String): Call<List<Product>>

    @GET("products.php")
    fun getProduct(@Query("id") proId: String): Call<Product>

    @POST("products.php")
    fun addProduct(@Body product: Product): Call<String>

    @POST("products.php?")
    fun updateProduct(@Query("id") id: String, @Body product: Product): Call<String>

    @DELETE("products.php")
    fun deleteProduct(@Query("id") id: String): Call<String>

    @Multipart
    @POST("images.php")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("product_id") desc: RequestBody
    ): Call<String>


    //car API partion
    @POST("orders.php")
    fun createCart(@Body order: Order): Call<Int>

    @POST("orderDetails.pho")
    fun addOrderDetails(@Body orderDetails: OrderDetails): Call<String>

    @POST("orders.pho")
    fun buy(@Body id: String): Call<String>

    @GET("order_details.php?")
    fun getUserCartItems(
        @Query("order_id") order_id: String
    ): Call<List<OrderDetails>>

    @DELETE("oder_details.php")
    fun deleteOrder(@Query("order_id") order_id: String): Call<String>
}