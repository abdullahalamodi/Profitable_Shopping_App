package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderDetails
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
    fun addOrder(order:OrderDetails):Call<String>{
       return shoppingApi.addOrderDetails(order)
    }

    fun deleteOrder(id:Int):Call<String>{
        return shoppingApi.deleteOrder(id.toString())
    }
    fun getOrders(cartId: Int):Call<List<OrderDetails>>{
        return shoppingApi.getUserCartItems(cartId.toString())
    }

    fun buy(cartId: Int):Call<String>{
        return shoppingApi.buy(cartId.toString())
    }
}