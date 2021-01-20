package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartRepositry(){
    private var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CategoryRepository.BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }
    fun createCart(order: Order): Call<Int> {
        return shoppingApi.createCart(order)

    }
    fun addOrder(order:OrderItem):Call<String>{
        return shoppingApi.addOrder(order)
    }
    fun getOrders(userId: String, cartId: Int):Call<List<OrderItem>>{
        return shoppingApi.getUserCartItems(userId,cartId)
    }
}