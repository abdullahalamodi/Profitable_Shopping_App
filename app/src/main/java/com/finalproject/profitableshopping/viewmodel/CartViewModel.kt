package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderItem
import com.finalproject.profitableshopping.data.repositories.CartRepositry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {
    private val cartIdLiveData = MutableLiveData<Int>()
    private val userIdLiveData = MutableLiveData<Int>()
    val pro= mutableListOf<OrderItem>()
    val cartRepositry: CartRepositry

    var orderListLiveData=Transformations.switchMap(cartIdLiveData){carId ->

        getCartItemList(userIdLiveData.value.toString(),carId)

    }

    init {
        cartRepositry= CartRepositry()
        pro.add(OrderItem(
            null ,
            "galexy",
            1,
            20,
            "red",
            "xl",
            2350.0
        ))
        pro.add(OrderItem(
            null ,
            "iphone",

            1,
            20,
            "red",
            "xl",
            2350.0
        ))
        pro.add(OrderItem(
            null ,
            "labtop",
            1,
            20,
            "red",
            "xl",
            2350.0
        ))
    }
    fun loadUser(useId: Int) {
        userIdLiveData.value = useId
    }
    fun loadOrder(orderId: Int) {
        cartIdLiveData.value = orderId

    }
    fun createCart(order: Order): LiveData<Int> {
        val cartId: MutableLiveData<Int> = MutableLiveData<Int>()
        val call=cartRepositry.createCart(order)
        call.enqueue(object :retrofit2.Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                cartId.value=response.body()
                Log.d("create cart",cartId.value.toString())
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d("faild creat cart",t.message!!)
                cartId.value=-1
            }

        })
        return cartId
    }

    fun addToCart(order: OrderItem) :LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call=cartRepositry.addOrder(order)
        call.enqueue(
            object :retrofit2.Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    message.value= response.body()
                    Log.d("success cart",response.body()!!)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("success cart",t.message!!)
                    message.value= message.value
                }
            }
        )
        return message
    }
    fun getCartItemList(userId:String,cartId:Int):LiveData<List<OrderItem>> {
        val orderList= MutableLiveData<List<OrderItem>>()
        val call=cartRepositry.getOrders(userId,cartId)
        call.enqueue(
            object :Callback<List<OrderItem>>{
                override fun onResponse(
                    call: Call<List<OrderItem>>,
                    response: Response<List<OrderItem>>
                ) {
                    orderList.value=response.body()
                    Log.d("success get order","success get orders")
                }

                override fun onFailure(call: Call<List<OrderItem>>, t: Throwable) {
                    Log.d("faild get order",t.message!!)
                    orderList.value= emptyList()
                }
            }
        )


        return orderList
    }
    fun changeOrderState(orderId: Int,isBought: Boolean):LiveData<String>{
        val message=MutableLiveData<String>()
        return message
    }

    fun deleteOrder(cartId: Int) :LiveData<String>{
        val message=MutableLiveData<String>()
        return message

    }

}