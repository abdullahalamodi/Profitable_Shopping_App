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

    fun uploadImage(image:MultipartBody.Part,
                    productId:RequestBody) :Call<String>{
        return shoppingApi.uploadImage(image,productId)

    }

    fun addProduct(product: Product): Call<String>{
        return shoppingApi.addProduct(product)
    }

    fun getProduct(proId:String):Call<Product>{
        return  shoppingApi.getProduct(proId)
    }

    fun getProductByIdOfCat(catId: Int?):Call<List<Product>>{
        return  shoppingApi.getProductsByIdOfCat(catId)
    }


    fun getProducts():Call<List<Product>>{
        return shoppingApi.getProducts()
    }

    fun getProductsFroManage():Call<List<Product>>{
        return shoppingApi.getProductsForMange("mange")
    }

    fun getUserProducts(userId:String): Call<List<Product>> {
        return shoppingApi.getUserProducts(userId)
    }

    fun getCategoryProducts(categoryId:String): Call<List<Product>> {
        return shoppingApi.getCategoryProducts(categoryId)
    }

    fun updateProduct(product: Product):Call<String>{
        return  shoppingApi.updateProduct(product.id.toString(),product)
    }

    fun updateProductCase(product: Product):Call<String>{
        return  shoppingApi.updateProductCase(product.id.toString(),product.isActive)
    }

    fun deleteProduct(proId:String):Call<String>{
        return shoppingApi.deleteProduct(proId)
    }

}