package com.finalproject.profitableshopping.data

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.CategoryModel


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShoppingRepositry {
     var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }
    fun createCategory(category: HashMap<Any,Any>): Call<String> {
      return shoppingApi.createCategory(category)
    }
    fun getCategory(id:Int):Call<CategoryModel>{
        return  shoppingApi.fetchCaregory(id)
    }
    fun getAllCategories():Call<List<CategoryModel>>{
        return shoppingApi.fetchAllCaregories()

    }

    fun updateCategory(catId:Int,category: HashMap<Any, Any>):Call<String>{
        return  shoppingApi.editCategory(catId,category)
    }
    fun deleteCategory(catId:Int):Call<String>{
        return shoppingApi.deleteCategory(catId)
    }
}
