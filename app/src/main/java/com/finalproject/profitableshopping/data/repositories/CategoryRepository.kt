package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Category


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryRepository {
     private var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }

    fun addCategory(category: HashMap<String,Any>): Call<String> {
      return shoppingApi.addCategory(category)
    }

    fun getCategory(id:Int):Call<Category>{
        return  shoppingApi.getCategoryById(id)
    }

    fun getCategoryByName(name:String):Call<Category>{
        return  shoppingApi.getCategoryByName(name)
    }


    fun getAllCategories():Call<List<Category>>{
        return shoppingApi.getCategories()

    }

    fun updateCategory(catId:Int,category: HashMap<String, Any>):Call<String>{
        return  shoppingApi.updateCategory(catId,category)
    }

    fun deleteCategory(catId:Int):Call<String>{
        return shoppingApi.deleteCategory(catId)
    }


    companion object{
        internal const val BASE_URL = "https://profitableshopping.000webhostapp.com/profitable_shopping_api/api/"
    }
}
