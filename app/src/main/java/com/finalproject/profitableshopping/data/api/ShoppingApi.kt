package com.finalproject.profitableshopping.data.api


import com.finalproject.profitableshopping.data.models.Complain
import com.finalproject.profitableshopping.data.models.*

import okhttp3.MultipartBody
import okhttp3.RequestBody


import retrofit2.Call
import retrofit2.http.*

interface ShoppingApi {

    //category methods
    @GET("categories.php")
    fun getCategories(): Call<List<Category>>

    @GET("categories.php")
    fun getCategoryById(@Query("id") query: String): Call<Category>

    @GET("categories.php")
    fun getCategoryByName(@Query("name") name: String): Call<Category>

    // @FormUrlEncoded
    @POST("categories.php")
    fun addCategory(@Body category: Category): Call<String>

    @POST("categories.php")
    fun updateCategory(@Query("id") id: String, @Body category: Category): Call<String>

    @DELETE("categories.php?")
    fun deleteCategory(@Query("id") id: String): Call<String>

    @POST("categories.php")
    fun updateCategoryCase(
        @Query("id") id: String,
        @Query("is_active") isActive: Int
    ): Call<String>


    @Multipart
    @POST("image.php")
    fun uploadCategoryImage(
        @Part image: MultipartBody.Part,
        @Part("category_id") desc: RequestBody
    ): Call<String>

    //product methods
    @GET("products.php")
    fun getProducts(): Call<List<Product>>

    //product methods
    @GET("products.php")
    fun getProductsForMange(@Query("manage") mange: String): Call<List<Product>>

    @GET("products.php")
    fun getUserProducts(@Query("user_id") userId: String): Call<List<Product>>

    @GET("products.php")
    fun getCategoryProducts(@Query("category_id") category_id: String): Call<List<Product>>

    @GET("products.php")
    fun getProduct(@Query("id") proId: String): Call<Product>

    @GET("products.php")
    fun getProductsByIdOfCat(@Query("id") categoryId: Int?): Call<List<Product>>

    @POST("products.php")
    fun addProduct(@Body product: Product): Call<String>

    @POST("products.php")
    fun updateProduct(@Query("id") id: String, @Body product: Product): Call<String>

    @POST("products.php")
    fun updateProductCase(
        @Query("id") id: String,
        @Query("is_active") isActive: Int
    ): Call<String>


    @DELETE("products.php")
    fun deleteProduct(@Query("id") id: String): Call<String>

    @Multipart
    @POST("images.php")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("product_id") desc: RequestBody
    ): Call<String>

    @Multipart
    @POST("images.php")
    fun uploadImages(
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part image3: MultipartBody.Part,
        @Part("product_id") desc: RequestBody
    ): Call<String>



    //cart API  method
    @POST("orders.php")
    fun createCart(@Body order: Order): Call<String>

    @POST("order_details.php")
    fun addOrderDetails(@Body orderDetails: OrderDetails): Call<String>

    @POST("orders.php")
    fun buy(@Query("id") id: String): Call<String>

    @GET("order_details.php?")
    fun getUserCartItems(
        @Query("order_id") order_id: String,
        @Query("user_id") user_id: String
    ): Call<List<OrderDetails>>


    @GET("order_details.php?")
    fun getCartItems(
        @Query("id") user_id: String
    ): Call<List<OrderDetails>>
    @GET("orders.php?")
    fun getUserOrders(@Query("user_id")userId: String): Call<List<Order>>
    @DELETE("orders.php")
    fun deleteOrder(@Query("id") orderId: String): Call<String>

    @DELETE("order_details.php")
    fun removeOrderDetails(@Query("id") id: String): Call<String>


    // favorite method
    @POST("favorite.php")
    fun addFavorite(@Body favorite: Favorite): Call<String>

    @GET("favorite.php?")
    fun getUserFavorites(@Query("user_id") userId:String): Call<List<Favorite>>

    @DELETE("favorite.php")
    fun deleteFavorite(@Query("id") favoriteId: Int): Call<String>

    // comment method
    @GET("comments.php")
    fun getProductComments(@Query("product_id") userId: String): Call<List<Comment>>

    @GET("comments.php")
    fun getUserComments(@Query("user_id") userId: String): Call<List<Comment>>

    @GET("comments.php")
    fun getComment(@Query("id") commentId: Int): Call<Comment>

    @POST("comments.php")
    fun addComment(@Body comment: Comment): Call<String>

    @POST("comments.php")
    fun updateComment(@Query("id") id: String, @Body comment: Comment): Call<String>

    @DELETE("comments.php")
    fun deleteComment(@Query("id") id: String): Call<String>

    // complain method API
    @POST("complains.php")
    fun addComplain(@Body complain: Complain):Call<String>
    @GET("complains.php")
    fun getComplains():Call<List<Complain>>
    @GET()
    fun getComplain(@Query("id") complainId:Int):Call<Complain>

    // Reports method API
    @POST("reports.php")
    fun addReport(@Body report:Report):Call<String>
    @GET("reports.php")
    fun getUserReports(@Query("to_id") userId:String):Call<List<Report>>
    @GET("reports.php")
    fun getProductReports(@Query("product_id") productId:String):Call<List<Report>>
    @GET("products.php/my_sales?")
    fun getUserSales(@Query("my_sales") userId: String):Call<List<MySales>>


}