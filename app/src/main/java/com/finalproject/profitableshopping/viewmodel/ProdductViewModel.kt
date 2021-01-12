package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.data.repositories.ProductRepositry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdductViewModel:ViewModel() {
    val productRepositry: ProductRepositry
    val productsListLiveData: LiveData<List<Product>>

    private val productIdLiveData = MutableLiveData<Int>()
    private val userIdLiveData = MutableLiveData<Int>()

    var productIDetailsLiveData = Transformations.switchMap(productIdLiveData){ proId ->
        getProduct(proId)
    }
    var userProductsListLiveData:LiveData<List<Product>>
    init {
        productRepositry = ProductRepositry()
        productsListLiveData=getProducts()
        this.userProductsListLiveData = Transformations.switchMap(userIdLiveData) { useId ->
            getUserProducts(useId)

        }



    }
    fun getProducts(): MutableLiveData<List<Product>>{
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call=productRepositry.getProducts()
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value=response.body()?: emptyList()
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Faild ",t.message!!)
            }
        })
        return responseLiveData
    }

    fun loadUser(useId:Int ) {
        userIdLiveData.value = useId
    }

    fun getUserProducts(userId: Int): MutableLiveData<List<Product>>{
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call=productRepositry.getUserProducts(userId )
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value=response.body()?: emptyList()
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Faild ",t.message!!)
            }
        })
        return responseLiveData
    }

    fun loadProduct(proId:Int ) {
        productIdLiveData.value = proId
    }

    private fun getProduct(proId:Int):MutableLiveData<Product>{
        val responseLiveData: MutableLiveData<Product> = MutableLiveData()
        val call= productRepositry.getProduct(proId)
        call.enqueue(object :Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                responseLiveData.value=response.body()
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {

            }

        })
        return responseLiveData
    }

    fun addProduct(product: HashMap<String, Any>): String {
        var resulte = "0"
        val call = productRepositry.addProduct(product)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                resulte = response.body()!!
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                resulte = t.message!!
            }
        })
        return  resulte
    }
    fun updateProduct(proId:Int,product: HashMap<String, Any>):MutableLiveData<String>{
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call= productRepositry.updateProduct(proId,product)
        call.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value=response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })
        return responseLiveData


    }
    fun deleteProduct(proId:Int):MutableLiveData<String>{
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call= productRepositry.deleteProduct(proId)
        call.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value=response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })
        return responseLiveData


    }

}