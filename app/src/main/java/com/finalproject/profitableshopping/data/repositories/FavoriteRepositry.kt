package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Favorite
import com.finalproject.profitableshopping.data.models.FavoriteDetails
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
    fun createFavorite(favorite: Favorite): Call<String> {
        return shoppingApi.createFavorite(favorite)

    }
    fun addFavoriteItem(favoriteDetails: FavoriteDetails): Call<String> {
        return shoppingApi.addFavoriteItem(favoriteDetails)
    }
    fun getOrders( favoriteId: Int): Call<List<FavoriteDetails>> {
        return shoppingApi.getFavoriteItems(favoriteId)
    }
    fun deleteFavoriteItem(favoriteItemId:Int):Call<String>{
        return shoppingApi.deleteFavoriteItem(favoriteItemId)
    }
}