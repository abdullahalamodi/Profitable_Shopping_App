package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.MySales
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.data.repositories.ProductRepositry
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    val productRepositry: ProductRepositry
    private val loadTrigger = MutableLiveData(Unit)
    private val userLoadTrigger = MutableLiveData<String>()
    private val categoryLoadTrigger = MutableLiveData<String>()
    private val manageLoadTrigger = MutableLiveData<Unit>()
    private val productIdLiveData = MutableLiveData<String>()
    private val userIdLiveData = MutableLiveData<Int>()
    private var product: Product? = null

    var productIDetailsLiveData = Transformations.switchMap(productIdLiveData) { proId ->
        getProduct(proId)
    }

    var productsListLiveData = Transformations.switchMap(loadTrigger) {
        getProducts()
    }

    var userProductsLiveData = Transformations.switchMap(userLoadTrigger) { userId ->
        getUserProducts(userId)
    }

    var categoryProductsLiveData = Transformations.switchMap(categoryLoadTrigger) { categoryId ->
        getCategoryProducts(categoryId)
    }

    var manageProductsLiveData = Transformations.switchMap(manageLoadTrigger) {
        getProductsForMange()
    }

    init {
        productRepositry = ProductRepositry()
        refresh()
    }

    fun refresh() {
        loadTrigger.value = Unit
    }

    fun refreshMangeList() {
        manageLoadTrigger.value = Unit
    }

    fun refreshUserList(userId: String) {
        userLoadTrigger.value = userId
    }

    fun refreshCategoryList(categoryId: String) {
        categoryLoadTrigger.value = categoryId
    }

    fun loadProduct(proId: String) {
        productIdLiveData.value = proId
    }

    private fun getProducts(): MutableLiveData<List<Product>> {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call = productRepositry.getProducts()
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
                Log.d("get products","success")
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Failed ", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData
    }

    private fun getProductsForMange(): MutableLiveData<List<Product>> {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call = productRepositry.getProductsFroManage()
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Failed ", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData
    }


    fun loadUser(useId: Int) {
        userIdLiveData.value = useId
    }

    private fun getUserProducts(userId: String): MutableLiveData<List<Product>> {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call = productRepositry.getUserProducts(userId)
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Failed ", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData
    }

    private fun getCategoryProducts(categoryId: String): MutableLiveData<List<Product>> {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call = productRepositry.getCategoryProducts(categoryId)
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Failed ", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData
    }



    private fun getProduct(proId: String): MutableLiveData<Product> {
        val responseLiveData: MutableLiveData<Product> = MutableLiveData()
        val call = productRepositry.getProduct(proId)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                responseLiveData.value = Product()
                Log.d("Failed ", t.message!!)

            }

        })
        return responseLiveData
    }

    fun addProduct(product: Product): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = productRepositry.addProduct(product)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
                Log.d("success", responseLiveData.value.toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("failed", t.message!!)
                responseLiveData.value = "-1"

            }
        })
        return responseLiveData
    }

    fun updateProductCase(
        product: Product
    ): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = productRepositry.updateProductCase(product)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                responseLiveData.value = response.body()
                response.body()?.let { Log.d("success update case", it) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message
                t.message?.let { Log.d("failed update case", it) }
            }
        })
        return responseLiveData

    }
    fun updateProduct(
        product: Product
    ): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = productRepositry.updateProduct(product)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                responseLiveData.value = response.body()
                response.body()?.let { Log.d("success update", it) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message
                t.message?.let { Log.d("failed update", it) }
            }
        })
        return responseLiveData

    }


    fun deleteProduct(proId: String,isActive:Int): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = productRepositry.deleteProduct(proId,isActive)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                responseLiveData.value = response.body()
                response.body()?.let { Log.d("success delete", it) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message
                t.message?.let { Log.d("failed delete", it) }
            }
        })
        return responseLiveData

    }

    fun uploadImage(
        image: MultipartBody.Part,
        productId: RequestBody
    ): Call<String> {
        return productRepositry.uploadImage(image, productId)
    }

    fun uploadImages(
        image1: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part,
        productId: RequestBody
    ): Call<String> {
        return productRepositry.uploadImages(image1,image2,image3, productId)
    }
    fun getMySales(userId: String):MutableLiveData<List<MySales>>{
        val responseLiveData: MutableLiveData<List<MySales>> = MutableLiveData()
        val call = productRepositry.getUserSales(userId)
        call.enqueue(object : Callback<List<MySales>> {
            override fun onResponse(
                call: Call<List<MySales>>,
                response: Response<List<MySales>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
                Log.d("Succees  ", responseLiveData.value!!.size.toString()!!)
            }

            override fun onFailure(call: Call<List<MySales>>, t: Throwable) {
                Log.d("Failed ", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData



    }

}