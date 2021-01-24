package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderDetails
import com.finalproject.profitableshopping.data.repositories.CartRepositry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {
    private val cartIdLiveData = MutableLiveData<Int>()
//    val pro = mutableListOf<OrderDetails>()
    val cartRepositry: CartRepositry

    var orderDetailsListLiveData = Transformations.switchMap(cartIdLiveData){ carId ->
        getCartItemList(carId)
    }

    init {
        cartRepositry= CartRepositry()
    }


    fun loadOrder(orderId: Int) {
        cartIdLiveData.value = orderId
    }


    fun createCart(order: Order): LiveData<String> {
        val cartId: MutableLiveData<String> = MutableLiveData<String>()
        val call = cartRepositry.createCart(order)
        call.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                cartId.value=response.body()
                Log.d("create cart",cartId.value.toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("faild creat cart",t.message!!)
                cartId.value= "-1"
            }

        })
        return cartId
    }

    fun addToCart(order: OrderDetails) :LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call = cartRepositry.addOrder(order)
        call.enqueue(
            object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    message.value= response.body()
                    Log.d("success cart",response.body()?:"")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("success cart",t.message!!)
                    message.value= message.value
                }
            }
        )
        return message
    }

    fun buy(id: String) :LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call = cartRepositry.buy(id)
        call.enqueue(
            object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    message.value= response.body()
                    Log.d("success add item",response.body()!!)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("failed  add item",t.message!!)
                    message.value= t.message
                }
            }
        )
        return message
    }
    private fun getCartItemList(cartId:Int):LiveData<List<OrderDetails>> {
        val orderList= MutableLiveData<List<OrderDetails>>()
        val call=cartRepositry.getOrders(cartId)
        call.enqueue(
            object :Callback<List<OrderDetails>>{
                override fun onResponse(
                    call: Call<List<OrderDetails>>,
                    response: Response<List<OrderDetails>>
                ) {
                    orderList.value = response.body()?: emptyList()
                    Log.d("success get order","success get orders")
                }

                override fun onFailure(call: Call<List<OrderDetails>>, t: Throwable) {
                    Log.d("faild get order",t.message!!)
                    orderList.value= emptyList()
                }
            }
        )


        return orderList
    }

    fun deleteCart(catId: String): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = cartRepositry.deleteOrder(catId)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
                Log.d("delet cat",response.body()!!)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message!!
                Log.d("delet failed",t.message!!)
            }
        })
        return responseLiveData
    }
    fun changeOrderState(order_id: Int,is_paid: Boolean):LiveData<String>{
        val message= MutableLiveData<String>()
        return message
    }

}