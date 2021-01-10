package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.ShoppingRepositry

import com.finalproject.profitableshopping.data.models.CategoryModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel :ViewModel() {
    val repositry: ShoppingRepositry
    val categoriesLiveData: LiveData<List<CategoryModel>>
    private val categoryIdLiveData = MutableLiveData<Int>()
    var categoryIDetailsLiveData = Transformations.switchMap(categoryIdLiveData){catId ->
        getSingleCategory(catId)
    }

    init {
        repositry = ShoppingRepositry()
        categoriesLiveData=getAllCategories()

    }
    fun loadNews(catId:Int ) {
        categoryIdLiveData.value = catId
    }
    fun createCategory(category: HashMap<Any, Any>): String {
        var resulte = "0"
        var call = repositry.createCategory(category)
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


    fun getAllCategories(): MutableLiveData<List<CategoryModel>>{
        val responseLiveData: MutableLiveData<List<CategoryModel>> = MutableLiveData()
        var call=repositry.getAllCategories()
        call.enqueue(object :Callback<List<CategoryModel>>{
            override fun onResponse(
                call: Call<List<CategoryModel>>,
                response: Response<List<CategoryModel>>
            ) {
                responseLiveData.value=response.body()?: emptyList()
            }
            override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                Log.d("categoryFaild",t.message!!)
            }
        })
        return responseLiveData
    }

    fun getSingleCategory(id:Int):MutableLiveData<CategoryModel>{
        val responseLiveData: MutableLiveData<CategoryModel> = MutableLiveData()
       var call= repositry.getCategory(id)
        call.enqueue(object :Callback<CategoryModel>{
            override fun onResponse(call: Call<CategoryModel>, response: Response<CategoryModel>) {
                responseLiveData.value=response.body()
            }

            override fun onFailure(call: Call<CategoryModel>, t: Throwable) {

            }

        })
        return responseLiveData
    }


    fun editeCategory(catId:Int,category: HashMap<Any, Any>):MutableLiveData<String>{
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        var call= repositry.updateCategory(catId,category)
        call.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value=response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })
        return responseLiveData


    }
    fun deleteCategory(catId:Int):MutableLiveData<String>{
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        var call= repositry.deleteCategory(catId)
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




