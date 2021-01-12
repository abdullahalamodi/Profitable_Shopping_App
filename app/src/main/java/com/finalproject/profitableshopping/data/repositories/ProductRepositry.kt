package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductRepositry {
    private var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CategoryRepository.BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }



    fun uploadImage(file: MultipartBody.Part,
                    name: RequestBody,
                    pId:Int,
                    uId:Int) :Call<Unit>{

        return shoppingApi.uploadImage(file,name,pId,uId)

    }

    fun AddProduct(product: HashMap<String,Any>): Call<String> {
        return shoppingApi.addProduct(product)
    }

    fun getProduct(proId:Int):Call<Product>{
        return  shoppingApi.getProduct(proId)
    }


    fun getProducts():Call<List<Product>>{
        return shoppingApi.getProducts()

    }

    fun getUserProducts(userId:Int): Call<List<Product>> {
        return shoppingApi.getUserProducts(userId)

    }

    fun updateProduct(proId:Int,product: HashMap<String, String>):Call<String>{
        return  shoppingApi.updateProduct(proId,product)
    }

    fun deleteProduct(proId:Int):Call<String>{
        return shoppingApi.deleteProduct(proId)
    }

}