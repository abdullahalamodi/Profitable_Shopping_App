package com.finalproject.profitableshopping.data.api



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

  //car API  method
    @POST("order.php")
    fun createCart(@Body order: Order):Call<Int>
    @POST("order.pho")
    fun addOrder(@Body orderItem:OrderDetails):Call<String>
    @DELETE("order.pho")
    fun deleteOrder(@Query("orderItem_id") orderItem:Int):Call<String>

    @GET("order.php ?")
    fun getUserCartItems(@Query("user_id") userId: String, @Query("order_id") orderId: Int):Call<List<OrderDetails>>
    // favorite method
    @POST("favorite.php")
    fun createFavorite(@Body favorite: Favorite):Call<Int>
    @POST("favorite.pho")
    fun addFavoriteItem(@Body FavoriteItem:FavoriteItem):Call<String>

    @GET("favorite.php ?")
    fun getFavoriteItems( @Query("favorite_id") favoriteId: Int):Call<List<FavoriteItem>>
    @DELETE("favorite.pho")
    fun deleteFavoriteItem(@Query("FavoriteItem_id") FavoriteItemId:Int):Call<String>

    // comment method

    @GET("comments.php")
    fun getComments(): Call<List<Comment>>
    @GET("comments.php")
    fun getUserComments(@Query("user_id") userId: String): Call<List<Comment>>
    @GET("comments.php")
    fun getComment(@Query("id") commentId: Int): Call<Comment>
    @POST("comments.php")
    fun addComment(@Body comment: Comment):Call<String>
    @POST("comments.php")
    fun updateComment(@Query("id") id:String, @Body comment: Comment):Call<String>
    @DELETE("comments.php")
    fun deleteComment(@Query("id")id:String):Call<String>
}