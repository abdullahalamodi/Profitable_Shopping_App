package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.FavoritResponse
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

    fun addFavoriteItem(favorite: Favorite): Call<String> {
        return shoppingApi.addFavorite(favorite)
    }
    fun getUserFavorite( userId: String): Call<List<Favorite>> {
        return shoppingApi.getUserFavorites(userId)
    }
    fun deleteFavoriteItem(favoriteId:Int):Call<String>{
        return shoppingApi.deleteFavorite(favoriteId)
    }
}