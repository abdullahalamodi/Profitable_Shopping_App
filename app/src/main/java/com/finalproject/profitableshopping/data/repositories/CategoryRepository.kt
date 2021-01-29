package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Category
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryRepository {
     private var shoppingApi: ShoppingApi

    init {
        val  gson=GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }

    fun addCategory(category: Category): Call<String> {
      return shoppingApi.addCategory(category)
    }

    fun getCategory(id:Int):Call<Category>{
        return  shoppingApi.getCategoryById(id.toString())
    }

    fun getCategoryByName(name:String):Call<Category>{
        return  shoppingApi.getCategoryByName(name)
    }


    fun getAllCategories():Call<List<Category>>{
        return shoppingApi.getCategories()

    }

    fun updateCategory(catId:Int?,category: Category):Call<String>{
        return  shoppingApi.updateCategory(catId.toString(),category)
    }

    fun deleteCategory(catId:Int):Call<String>{
        return shoppingApi.deleteCategory(catId.toString())
    }

    fun updateCategoryCase(catId:Int,isActive:Int):Call<String>{
        return shoppingApi.updateCategoryCase(catId.toString(),isActive)
    }

    fun uploadImage(image: MultipartBody.Part,
                    catId: RequestBody
    ) :Call<String>{
        return shoppingApi.uploadCategoryImage(image,catId)

    }

    companion object{
       /// internal const val BASE_URL = "http://192.168.1.33:80/profitable_shopping_api/api/"
       // internal const val BASE_URL = "https://profitableshopping.000webhostapp.com/profitable_shopping_api/api/"
//     internal const val BASE_URL = "http://192.168.191.1:8080/profitable_shopping_api/api/"
        internal const val BASE_URL = "http://10.0.2.2:80/profitable_shopping_api/api/"
    }

}

