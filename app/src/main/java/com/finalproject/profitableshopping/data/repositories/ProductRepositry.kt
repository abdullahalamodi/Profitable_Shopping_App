package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.stream.IntStream

class ProductRepositry {
    private var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CategoryRepository.BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }



    fun uploadImage(file:List< MultipartBody.Part>,
                    pId:String,
                    uId:String) :Call<String>{

        return shoppingApi.uploadImage(file,pId,uId)

    }

    fun addProduct(product: Product): Call<String>{
        return shoppingApi.addProduct(product)
    }

    fun getProduct(proId:String):Call<Product>{
        return  shoppingApi.getProduct(proId)
    }


    fun getProducts():Call<List<Product>>{
        return shoppingApi.getProducts()

    }

    fun getUserProducts(userId:String): Call<List<Product>> {
        return shoppingApi.getUserProducts(userId)

    }

    fun updateProduct(proId:String,product: Product):Call<String>{
        return  shoppingApi.updateProduct(proId,product)
    }

    fun deleteProduct(proId:String):Call<String>{
        return shoppingApi.deleteProduct(proId)
    }

}