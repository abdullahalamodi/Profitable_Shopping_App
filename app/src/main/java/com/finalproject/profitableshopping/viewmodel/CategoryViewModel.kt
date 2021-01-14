package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.repositories.CategoryRepository

import com.finalproject.profitableshopping.data.models.Category

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel() {
    val repository: CategoryRepository
    var categoriesLiveData: LiveData<List<Category>>
    private val categoryIdLiveData = MutableLiveData<Int>()
    var categoryDetailsLiveData = Transformations.switchMap(categoryIdLiveData) { catId ->
        getCategory(catId)
    }

    private val loadTrigger = MutableLiveData(Unit)

    init {
        repository =
            CategoryRepository()
        categoriesLiveData = Transformations.switchMap(loadTrigger) {
            getCategories()
        }
        refresh()
    }

    fun refresh() {
        loadTrigger.value = Unit
    }


    fun addCategory(category: Category): MutableLiveData<String> {
        val responseLiveData = MutableLiveData<String>()
        val call = repository.addCategory(category)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message!!
            }
        })
        return responseLiveData
    }


    private fun getCategories(): MutableLiveData<List<Category>> {
        val responseLiveData: MutableLiveData<List<Category>> = MutableLiveData()
        val call = repository.getAllCategories()
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("categoryFailed", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData
    }

    private fun getCategory(id: Int): MutableLiveData<Category> {
        val responseLiveData: MutableLiveData<Category> = MutableLiveData()
        val call = repository.getCategory(id)
        call.enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                responseLiveData.value = response.body() ?: Category(id = -1)
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("categoryFailed", t.message!!)
                responseLiveData.value = Category(id = -1)
            }
        })
        return responseLiveData
    }

    fun getCategoryByName(name: String): MutableLiveData<Category> {
        val responseLiveData: MutableLiveData<Category> = MutableLiveData()
        val call = repository.getCategoryByName(name)
        call.enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                responseLiveData.value = response.body() ?: Category(id = -1)
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("categoryFailed", t.message!!)
                responseLiveData.value = Category(id = -1)
            }

        })
        return responseLiveData
    }

    fun updateCategory(catId: Int?, category: Category): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = repository.updateCategory(catId, category)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message!!
            }
        })
        return responseLiveData

    }

    fun deleteCategory(catId: Int): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = repository.deleteCategory(catId)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message!!
            }
        })
        return responseLiveData
    }
}




