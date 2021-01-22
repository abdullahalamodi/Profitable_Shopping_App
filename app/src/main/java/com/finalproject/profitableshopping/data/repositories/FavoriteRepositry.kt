package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Favorite
import com.finalproject.profitableshopping.data.models.FavoriteItem
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderDetails
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoriteRepositry {

    private var shoppingApi: ShoppingApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CategoryRepository.BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }
    fun createFavorite(favorite: Favorite): Call<Int> {
        return shoppingApi.createFavorite(favorite)

    }
    fun addFavoriteItem(favoriteItem: FavoriteItem): Call<String> {
        return shoppingApi.addFavoriteItem(favoriteItem)
    }
    fun getOrders( favoriteId: Int): Call<List<FavoriteItem>> {
        return shoppingApi.getFavoriteItems(favoriteId)
    }
    fun deleteFavoriteItem(favoriteItemId:Int):Call<String>{
        return shoppingApi.deleteFavoriteItem(favoriteItemId)
    }
}